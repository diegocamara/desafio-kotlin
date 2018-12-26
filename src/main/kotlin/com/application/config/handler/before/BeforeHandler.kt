package com.application.config.handler.before

import com.application.config.exception.BusinessException
import io.javalin.Javalin
import org.eclipse.jetty.http.HttpStatus

object BeforeHandler {

    fun register(app: Javalin) {

        app.before("/api/users/:id") {
            it.header("Authorization") ?: throw BusinessException("Não autorizado", HttpStatus.UNAUTHORIZED_401)
        }

    }

}