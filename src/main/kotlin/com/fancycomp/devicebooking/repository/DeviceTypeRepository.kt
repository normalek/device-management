package com.fancycomp.devicebooking.repository

import com.fancycomp.devicebooking.entity.Device
import com.fancycomp.devicebooking.entity.DeviceType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceTypeRepository : JpaRepository<DeviceType, Int>