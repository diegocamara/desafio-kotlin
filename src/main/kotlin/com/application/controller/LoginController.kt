package com.application.controller

import com.application.config.exception.BusinessException
import com.application.dao.PhoneDAO
import com.application.dao.UserDAO
import com.application.domain.UserDTO
import com.application.dto.LoginDTO
import com.application.service.LoginService
import com.application.service.PhoneService
import com.application.service.UserService
import io.javalin.Context

object LoginController {

    private val loginService: LoginService = LoginService(UserService(UserDAO(), PhoneService(PhoneDAO())))

    fun login(ctx: Context) {
        val loginDTO = ctx.body<LoginDTO>()
        validLoginFields(loginDTO)
        val userLogged = loginService.login(loginDTO)
        ctx.json(userLogged)
    }


    private fun validLoginFields(loginDTO: LoginDTO) {

        when {
            loginDTO?.email.isNullOrEmpty() -> throw BusinessException("Campo email deve ser preenchido")
            loginDTO?.password.isNullOrEmpty() -> throw BusinessException("Campo password deve ser preenchido")
        }


    }


}