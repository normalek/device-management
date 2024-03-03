package com.fancycomp.devicebooking.mapper

import com.fancycomp.devicebooking.dto.DeviceDto
import com.fancycomp.devicebooking.entity.Device

class DeviceMapper {
    companion object {
        fun toDeviceDto(entity: Device):DeviceDto {
            return DeviceDto(
                id = entity.id,
                isAvailable = entity.isAvailable,
                bookedDateTime = entity.bookedDateTime,
                releasedDateTime = entity.releasedDateTime,
                deviceTypeId = entity.deviceType.id,
                deviceTypeName = entity.deviceType.name,
                bookedBy = entity.bookedBy
            )
        }
    }
}