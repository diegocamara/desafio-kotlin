package com.application

import io.javalin.security.Role

enum class ApiRole : Role {
    ANYONE, USER_READ, USER_WRITE
}