package com.fancycomp.devicebooking.dto.exception

class DeviceAlreadyReleasedException(id: Int) : RuntimeException("Device with id $id is already released")