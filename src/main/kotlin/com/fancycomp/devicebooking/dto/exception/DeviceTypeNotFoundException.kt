package com.fancycomp.devicebooking.dto.exception

class DeviceTypeNotFoundException(id: Int) : RuntimeException("Not found device type with id $id")