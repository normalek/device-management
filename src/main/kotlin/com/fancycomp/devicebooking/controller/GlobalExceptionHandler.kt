package com.fancycomp.devicebooking.controller

import com.fancycomp.devicebooking.dto.exception.DeviceNotFoundException
import com.fancycomp.devicebooking.dto.ErrorResponse
import com.fancycomp.devicebooking.dto.exception.DeviceAlreadyBookedException
import com.fancycomp.devicebooking.dto.exception.DeviceAlreadyReleasedException
import com.fancycomp.devicebooking.dto.exception.DeviceTypeNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(value = [DeviceNotFoundException::class, DeviceTypeNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleNotFoundException(ex: RuntimeException): ErrorResponse {
        return ErrorResponse(error = "${ex.message}", status = HttpStatus.NOT_FOUND.value())
    }

    @ExceptionHandler(value = [DeviceAlreadyBookedException::class, DeviceAlreadyReleasedException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleBadRequestException(ex: RuntimeException): ErrorResponse {
        return ErrorResponse(error = "${ex.message}", status = HttpStatus.BAD_REQUEST.value())
    }
}