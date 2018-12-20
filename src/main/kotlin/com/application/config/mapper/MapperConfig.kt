package com.application.config.mapper

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.json.JavalinJackson
import java.text.SimpleDateFormat

fun configureMapper() {

    JavalinJackson.configure(
        jacksonObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).setDateFormat(
            SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        ).registerModule(JodaModule())
    )
}