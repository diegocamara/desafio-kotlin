package com.application.extension.string

import io.javalin.json.JavalinJackson

fun <T> String.toJsonObject(valueType: Class<T>): T {
    return JavalinJackson.getObjectMapper().readValue(this, valueType)
}