package com.fancycomp.devicebooking.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Device(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(name = "is_available")
    var isAvailable: Boolean,

    @Column(name = "booked_date_time")
    var bookedDateTime: LocalDateTime? = null,

    @Column(name = "released_date_time")
    var releasedDateTime: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "device_type", referencedColumnName = "id")
    val deviceType: DeviceType,

    @Column(name = "booked_by")
    var bookedBy: String? = null
)