package com.application.util

import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.BadRequestResponse
import io.javalin.Context
import io.javalin.json.JavalinJackson
import io.javalin.json.JavalinJson
import io.javalin.validation.TypedValidator
import org.joda.time.DateTime
import java.time.LocalDateTime

fun <T> String.toJsonObject(valueType: Class<T>): T {
    return JavalinJackson.getObjectMapper().readValue(this, valueType)
}