package com.application.config.exception

import com.application.config.exception.response.ApplicationErrorResponse
import org.eclipse.jetty.http.HttpStatus

open class BusinessException(message: String, statusCode: Int = HttpStatus.BAD_REQUEST_400) :
    RuntimeException(message) {

    val error: ApplicationErrorResponse = ApplicationErrorResponse(message)
    val statusCode: Int = statusCode

}



