package com.application.service

import com.application.config.exception.BusinessException
import com.application.domain.UserDTO
import com.application.dto.LoginDTO
import org.eclipse.jetty.http.HttpStatus
import org.joda.time.DateTime

class LoginService(private val userService: UserService) {

    fun login(loginDTO: LoginDTO): UserDTO? {

        val userDTO: UserDTO? = userService.findByEmail(email = loginDTO.email) ?: throw BusinessException(
            "Usuário e/ou senha inválidos",
            HttpStatus.NOT_FOUND_404
        )

        if (!userDTO?.password.equals(loginDTO?.password)) {
            throw BusinessException("Usuário e/ou senha inválidos", HttpStatus.UNAUTHORIZED_401)
        }

        userDTO?.lastLogin = DateTime.now()

        return userDTO
    }
}