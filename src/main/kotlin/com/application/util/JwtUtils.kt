package com.application.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.javalin.json.JavalinJackson

object JwtUtils {


    fun sign(anyName: String, any: Any, maxAge: Int): String {
        val algorithm = Algorithm.HMAC256("superSecret")
        val anyValue = JavalinJackson.getObjectMapper().writeValueAsString(any)
        return JWT.create().withIssuer("Super Issue")
            .withClaim(anyName, anyValue).sign(algorithm)
    }

}