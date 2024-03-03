package com.fancycomp.devicebooking.utils

import com.fancycomp.devicebooking.entity.Device
import com.fancycomp.devicebooking.entity.DeviceType
import kotlin.random.Random

class TestDeviceUtils {
    companion object {
        fun getAvailableDevice(): Device {
            val testDeviceType = getTestDeviceType()
            return Device(
                id = Random.nextInt(100, 10000),
                isAvailable = true,
                deviceType = testDeviceType
            )
        }

        fun getAvailableDeviceWithTypeId(typeId: Int): Device {
            val testDeviceType = DeviceType(
                id = typeId,
                name = "TEST_TYPE"
            )
            return Device(
                id = Random.nextInt(100, 10000),
                isAvailable = true,
                deviceType = testDeviceType
            )
        }

        fun getTestDeviceType(): DeviceType {
            return DeviceType(
                id = Random.nextInt(100, 10000),
                name = "TEST_TYPE"
            )
        }
    }
}