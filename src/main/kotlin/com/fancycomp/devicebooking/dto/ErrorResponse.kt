package com.fancycomp.devicebooking.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.validation.FieldError

data class ErrorResponse(
    override val error: String,
    override val status: Int,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val code: String = "",
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val fieldErrors: List<FieldError> = emptyList(),
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val traceId: String? = null
) : RemoteError

interface RemoteError{
    val status: Int
    val error: String
}