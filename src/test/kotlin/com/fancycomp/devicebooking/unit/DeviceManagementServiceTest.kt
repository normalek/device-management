package com.fancycomp.devicebooking.unit

import com.fancycomp.devicebooking.dto.exception.DeviceAlreadyBookedException
import com.fancycomp.devicebooking.dto.exception.DeviceAlreadyReleasedException
import com.fancycomp.devicebooking.dto.exception.DeviceNotFoundException
import com.fancycomp.devicebooking.dto.exception.DeviceTypeNotFoundException
import com.fancycomp.devicebooking.entity.Device
import com.fancycomp.devicebooking.messaging.MessageProducer
import com.fancycomp.devicebooking.messaging.dto.DeviceEvent
import com.fancycomp.devicebooking.repository.DeviceRepository
import com.fancycomp.devicebooking.service.DeviceManagementServiceImpl
import com.fancycomp.devicebooking.utils.TestDeviceUtils.Companion.getAvailableDevice
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willDoNothing
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class DeviceManagementServiceTest {
    private val deviceRepository: DeviceRepository = mock()
    private val messageProducer: MessageProducer = mock()
    private val deviceManagementService = DeviceManagementServiceImpl(
        deviceRepository = deviceRepository,
        messageProducer = messageProducer
    )

    @Test
    fun `GIVEN no existing device WHEN booking it by id THEN throw DeviceNotFoundException`() {
        // GIVEN
        val testId = 1
        // WHEN
        val exception = assertThrows<DeviceNotFoundException> { deviceManagementService.bookDeviceById(testId, "TEST") }
        // THEN
        assertTrue(exception.message == "Not found device with id $testId")
    }

    @Test
    fun `GIVEN no existing device type WHEN booking it by device_type_id THEN throw DeviceTypeNotFoundException`() {
        // GIVEN
        val testId = 1
        // WHEN
        val exception = assertThrows<DeviceTypeNotFoundException> { deviceManagementService.bookDeviceByDeviceType(testId, "TEST") }
        // THEN
        assertTrue(exception.message == "Not found device type with id $testId")
    }

    @Test
    fun `GIVEN no existing device WHEN releasing it by id THEN throw DeviceTypeNotFoundException`() {
        // GIVEN
        val testId = 1
        // WHEN
        val exception = assertThrows<DeviceNotFoundException> { deviceManagementService.releaseDeviceById(testId) }
        // THEN
        assertTrue(exception.message == "Not found device with id $testId")
    }

    @Test
    fun `GIVEN booked device WHEN booking it by id THEN throw DeviceAlreadyBookedException`() {
        // GIVEN
        val testDevice = getAvailableDevice()
        testDevice.isAvailable = false
        given(deviceRepository.findById(testDevice.id)).willReturn(Optional.of(testDevice))
        // WHEN
        val exception = assertThrows<DeviceAlreadyBookedException> { deviceManagementService.bookDeviceById(testDevice.id, "TEST") }
        // THEN
        assertTrue(exception.message == "Device with id ${testDevice.id} is already booked")
    }

    @Test
    fun `GIVEN available device WHEN release it by id THEN throw DeviceAlreadyBookedException`() {
        // GIVEN
        val testDevice = getAvailableDevice()
        given(deviceRepository.findById(testDevice.id)).willReturn(Optional.of(testDevice))
        // WHEN
        val exception = assertThrows<DeviceAlreadyReleasedException> { deviceManagementService.releaseDeviceById(testDevice.id) }
        // THEN
        assertTrue(exception.message == "Device with id ${testDevice.id} is already released")
    }

    @Test
    fun `GIVEN available device WHEN booking it by device_type_id THEN success`() {
        // GIVEN
        val testDevice = getAvailableDevice()
        given(deviceRepository.findAllByDeviceType_Id(testDevice.deviceType.id)).willReturn(listOf(testDevice))
        given(deviceRepository.save(testDevice)).willReturn(testDevice)
        willDoNothing().given(messageProducer).sendEvent(DeviceEvent(testDevice.id, false))
        // WHEN
        val bookedDevice = deviceManagementService.bookDeviceByDeviceType(testDevice.deviceType.id, "TEST")
        // THEN
        assertFalse(bookedDevice.isAvailable)
        verify(deviceRepository, times(1)).save(any(Device::class.java))
    }

    @Test
    fun `GIVEN available device WHEN booking it by id THEN success`() {
        // GIVEN
        val testDevice = getAvailableDevice()
        given(deviceRepository.findById(testDevice.id)).willReturn(Optional.of(testDevice))
        given(deviceRepository.save(testDevice)).willReturn(testDevice)
        willDoNothing().given(messageProducer).sendEvent(DeviceEvent(testDevice.id, false))
        // WHEN
        val bookedDevice = deviceManagementService.bookDeviceById(testDevice.id, "TEST")
        // THEN
        assertFalse(bookedDevice.isAvailable)
        verify(deviceRepository, times(1)).save(any(Device::class.java))
    }

    @Test
    fun `GIVEN booked device WHEN releasing it by id THEN success`() {
        // GIVEN
        val testDevice = getAvailableDevice()
        testDevice.isAvailable = false
        given(deviceRepository.findById(testDevice.id)).willReturn(Optional.of(testDevice))
        given(deviceRepository.save(testDevice)).willReturn(testDevice)
        willDoNothing().given(messageProducer).sendEvent(DeviceEvent(testDevice.id, false))
        // WHEN
        val bookedDevice = deviceManagementService.releaseDeviceById(testDevice.id)
        // THEN
        assertTrue(bookedDevice.isAvailable)
        verify(deviceRepository, times(1)).save(any(Device::class.java))
    }

    @Test
    fun `GIVEN two devices WHEN get them THEN list of two devices`() {
        // GIVEN
        val testDeviceOne = getAvailableDevice()
        val testDeviceTwo = getAvailableDevice()
        given(deviceRepository.findAll()).willReturn(listOf(testDeviceOne, testDeviceTwo))
        // WHEN
        val allDevices = deviceManagementService.getAllDevices()
        // THEN
        assert(allDevices.size == 2)
    }


}