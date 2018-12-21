package com.application.config.exception

import com.application.config.exception.response.ApplicationErrorResponse
import java.lang.RuntimeException

open class BusinessException(message: String) : RuntimeException(message) {

    val error: ApplicationErrorResponse = ApplicationErrorResponse(message)

}



