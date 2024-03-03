package com.fancycomp.devicebooking.controller

import com.fancycomp.devicebooking.service.DeviceManagementService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Tag(name = "Device-management", description = "Device-management APIs")
@RestController
@RequestMapping("/api/v1/device/management")
class DeviceManagementController(private val deviceManagementService: DeviceManagementService) {

    @Operation(
        summary = "Get all devices",
        description = "Get all devices including their current status and other details")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "List of devices")
    )
    @GetMapping(path = ["/all"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllDevices(): ResponseEntity<*> {
        return ResponseEntity.ok(deviceManagementService.getAllDevices())
    }

    @Operation(
        summary = "Book a device by its id",
        description = "Book a device by its id. This operation only possible for a non booked device")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Booked device"),
        ApiResponse(responseCode = "400", description = "The device is already booked"),
        ApiResponse(responseCode = "404", description = "No device found by given id")
    )
    @PutMapping(path = ["/{id}/book"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun bookDeviceById(@PathVariable id: Int, principal: Principal): ResponseEntity<*> {
        return ResponseEntity.ok(deviceManagementService.bookDeviceById(id, principal.name))
    }

    @Operation(
        summary = "Book a device by device type id",
        description = "Book a device by device type id. This operation only possible for a non booked device")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Booked device"),
        ApiResponse(responseCode = "400", description = "The device is already booked"),
        ApiResponse(responseCode = "404", description = "No device found by given device type id")
    )
    @PutMapping(path = ["/device-type/{id}/book"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun bookDeviceByDeviceType(@PathVariable id: Int, principal: Principal): ResponseEntity<*> {
        return ResponseEntity.ok(deviceManagementService.bookDeviceByDeviceType(id, principal.name))
    }

    @Operation(
        summary = "Release a device by its id",
        description = "Release a device by its id. This operation only possible for a previously booked device")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Booked device"),
        ApiResponse(responseCode = "400", description = "The device is already released"),
        ApiResponse(responseCode = "404", description = "No device found by given id")
    )
    @PutMapping(path = ["/{id}/release"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun releaseDeviceById(@PathVariable id: Int): ResponseEntity<*> {
        return ResponseEntity.ok(deviceManagementService.releaseDeviceById(id))
    }
}