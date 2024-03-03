package com.fancycomp.devicebooking.unit

import com.fancycomp.devicebooking.mapper.DeviceMapper
import com.fancycomp.devicebooking.utils.TestDeviceUtils.Companion.getAvailableDevice
import org.junit.Test

class DeviceMapperTest {
    @Test
    fun `GIVEN test device entity WHEN mapping to Dto THEN should properly mapped all fields`() {
        // GIVEN
        val device = getAvailableDevice()
        // WHEN
        val deviceDto = DeviceMapper.toDeviceDto(device)
        // THEN
        assert(deviceDto.id == device.id)
        assert(deviceDto.isAvailable == device.isAvailable)
        assert(deviceDto.deviceTypeName == device.deviceType.name)
        assert(deviceDto.deviceTypeId == device.deviceType.id)
        assert(deviceDto.bookedBy == device.bookedBy)
        assert(deviceDto.bookedDateTime == device.bookedDateTime)
        assert(deviceDto.releasedDateTime == device.releasedDateTime)
    }
}