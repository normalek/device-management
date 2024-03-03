package com.fancycomp.devicebooking.dto

import java.time.LocalDateTime

data class DeviceDto(
    val id: Int,
    val isAvailable: Boolean,
    val bookedDateTime: LocalDateTime? = null,
    val releasedDateTime: LocalDateTime? = null,
    val deviceTypeId: Int,
    val deviceTypeName: String,
    val bookedBy: String? = null
)