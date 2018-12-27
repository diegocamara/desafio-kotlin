package com.application.controller

import com.application.config.exception.BusinessException
import com.application.dto.LoginDTO
import com.application.service.LoginService
import com.application.service.impl.LoginServiceImpl
import io.javalin.Context

class LoginController(private val loginService: LoginService) {

    fun login(ctx: Context) {
        val loginDTO = ctx.body<LoginDTO>()
        validLoginFields(loginDTO)
        val userLogged = loginService.login(loginDTO)
        ctx.json(userLogged!!)
    }


    private fun validLoginFields(loginDTO: LoginDTO) {

        when {
            loginDTO.email.isNullOrEmpty() -> throw BusinessException("Campo email deve ser preenchido")
            loginDTO.password.isNullOrEmpty() -> throw BusinessException("Campo password deve ser preenchido")
        }


    }


}