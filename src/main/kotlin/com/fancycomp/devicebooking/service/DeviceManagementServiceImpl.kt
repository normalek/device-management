package com.fancycomp.devicebooking.service

import com.fancycomp.devicebooking.dto.DeviceDto
import com.fancycomp.devicebooking.dto.exception.DeviceAlreadyBookedException
import com.fancycomp.devicebooking.dto.exception.DeviceAlreadyReleasedException
import com.fancycomp.devicebooking.dto.exception.DeviceNotFoundException
import com.fancycomp.devicebooking.dto.exception.DeviceTypeNotFoundException
import com.fancycomp.devicebooking.entity.Device
import com.fancycomp.devicebooking.mapper.DeviceMapper
import com.fancycomp.devicebooking.messaging.dto.DeviceEvent
import com.fancycomp.devicebooking.messaging.MessageProducer
import com.fancycomp.devicebooking.repository.DeviceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.stream.Collectors.toUnmodifiableList

@Service
class DeviceManagementServiceImpl(
    private val deviceRepository: DeviceRepository,
    private val messageProducer: MessageProducer): DeviceManagementService {

    @Transactional
    override fun bookDeviceById(id: Int, bookedBy: String):DeviceDto {
        val device = deviceRepository.findById(id).orElseThrow{ DeviceNotFoundException(id) }
        processBooking(device, bookedBy)
        return DeviceMapper.toDeviceDto(device)
    }

    @Transactional
    override fun bookDeviceByDeviceType(id: Int, bookedBy: String):DeviceDto {
        val device = deviceRepository.findAllByDeviceType_Id(id).ifEmpty { throw DeviceTypeNotFoundException(id) }.first()
        processBooking(device, bookedBy)
        return DeviceMapper.toDeviceDto(device)
    }

    private fun processBooking(device: Device, bookedBy: String) {
        if (!device.isAvailable) throw DeviceAlreadyBookedException(device.id)
        device.isAvailable = false
        device.bookedBy = bookedBy
        device.bookedDateTime = LocalDateTime.now()
        messageProducer.sendEvent(DeviceEvent(device.id, false))
        deviceRepository.save(device)
    }

    @Transactional
    override fun releaseDeviceById(id: Int):DeviceDto {
        val device = deviceRepository.findById(id).orElseThrow{DeviceNotFoundException(id)}
        if (device.isAvailable) throw DeviceAlreadyReleasedException(device.id)
        device.isAvailable = true
        device.bookedBy = null
        device.releasedDateTime = LocalDateTime.now()
        deviceRepository.save(device)
        messageProducer.sendEvent(DeviceEvent(id, true))
        return DeviceMapper.toDeviceDto(device)
    }

    @Transactional(readOnly = true)
    override fun getAllDevices(): List<DeviceDto> {
        return deviceRepository.findAll().stream().map { DeviceMapper.toDeviceDto(it) }.collect(toUnmodifiableList())
    }
}