package com.fancycomp.devicebooking.repository

import com.fancycomp.devicebooking.entity.Device
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : JpaRepository<Device, Int> {
    fun findAllByDeviceType_Id(id: Int): List<Device>
}