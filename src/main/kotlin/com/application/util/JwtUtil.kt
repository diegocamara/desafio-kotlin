package com.application.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.javalin.json.JavalinJackson
import java.util.*

object JwtUtil {

    fun sign(subject: Any, maxAgeInMinutes: Int): String {
        val algorithm = Algorithm.HMAC256("superSecret")
        val subjectValue = JavalinJackson.getObjectMapper().writeValueAsString(subject)
        return JWT.create().withIssuer("Super Issue").withSubject(subjectValue)
            .withExpiresAt(plusMinutes(maxAgeInMinutes)).sign(algorithm)
    }

    private fun plusMinutes(minutes: Int): Date {
        val oneMinuteInMillis = 60000
        val calendar = Calendar.getInstance()
        return Date(calendar.timeInMillis + (minutes * oneMinuteInMillis))
    }


}