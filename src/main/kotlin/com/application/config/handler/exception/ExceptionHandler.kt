package com.application.config.handler.exception

import com.application.config.exception.BusinessException
import com.application.config.exception.response.ApplicationErrorResponse
import com.fasterxml.jackson.annotation.JsonRootName
import io.javalin.Javalin
import org.eclipse.jetty.http.HttpStatus

@JsonRootName("errors")
class ErrorResponse : HashMap<String, Any>()

object ExceptionHandler {
    fun register(app: Javalin) {
        app.exception(Exception::class.java) { exception, ctx ->
            exception.printStackTrace()
            ctx.json(ApplicationErrorResponse(exception.message.toString())).status(HttpStatus.INTERNAL_SERVER_ERROR_500)
        }
        app.exception(BusinessException::class.java) { exception, ctx ->
            ctx.json(exception.error).status(exception.statusCode)
        }
    }
}