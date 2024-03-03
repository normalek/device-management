package com.fancycomp.devicebooking.dto.exception

class DeviceNotFoundException(id: Int) : RuntimeException("Not found device with id $id")