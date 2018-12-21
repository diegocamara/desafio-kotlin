package com.application.config.exception.handler

import com.application.config.exception.BusinessException
import com.application.config.exception.response.ApplicationErrorResponse
import com.fasterxml.jackson.annotation.JsonRootName
import io.javalin.BadRequestResponse
import io.javalin.HttpResponseException
import io.javalin.Javalin
import org.eclipse.jetty.http.HttpStatus

@JsonRootName("errors")
class ErrorResponse : HashMap<String, Any>()

object ExceptionHandler {
    fun register(app: Javalin) {
        app.exception(Exception::class.java) { exception, ctx ->
            exception.printStackTrace()
            val error = ErrorResponse()
            error["Error"] = listOf(exception.message)
            ctx.json(error).status(HttpStatus.INTERNAL_SERVER_ERROR_500)
        }
        app.exception(HttpResponseException::class.java) { exception, ctx ->
            val error = ApplicationErrorResponse(exception.message.toString())
            ctx.json(error).status(exception.status)
        }
        app.exception(BadRequestResponse::class.java) { exception, ctx ->
            val error = ApplicationErrorResponse(exception.message.toString())
            ctx.json(error).status(exception.status)
        }
        app.exception(BusinessException::class.java) { exception, ctx ->
            ctx.json(exception.error).status(HttpStatus.BAD_REQUEST_400)
        }
    }
}