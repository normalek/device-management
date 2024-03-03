package com.fancycomp.devicebooking.entity

import jakarta.persistence.*

@Entity
@Table(name = "device_type")
class DeviceType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    val name: String
)