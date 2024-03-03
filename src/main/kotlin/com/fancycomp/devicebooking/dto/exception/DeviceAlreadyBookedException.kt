package com.fancycomp.devicebooking.dto.exception

class DeviceAlreadyBookedException(id: Int) : RuntimeException("Device with id $id is already booked")