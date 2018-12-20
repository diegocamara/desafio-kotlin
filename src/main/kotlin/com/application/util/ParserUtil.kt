package com.application.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.DateTime
import java.time.LocalDateTime

fun <T> String.toJsonObject(valueType: Class<T>): T {
    return jacksonObjectMapper().readValue(this, valueType)
}