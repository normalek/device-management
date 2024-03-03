package com.fancycomp.devicebooking.service

import com.fancycomp.devicebooking.dto.DeviceDto

interface DeviceManagementService {
    fun bookDeviceById(id: Int, bookedBy: String):DeviceDto
    fun bookDeviceByDeviceType(id: Int, bookedBy: String):DeviceDto
    fun releaseDeviceById(id: Int):DeviceDto
    fun getAllDevices(): List<DeviceDto>
}