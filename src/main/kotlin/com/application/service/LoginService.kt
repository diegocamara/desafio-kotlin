package com.application.service

import com.application.config.exception.BusinessException
import com.application.domain.UserDTO
import com.application.dto.LoginDTO

class LoginService(private val userService: UserService) {

    fun login(loginDTO: LoginDTO): UserDTO {
        return userService.findByEmailAndPassword(email = loginDTO.email, password = loginDTO.password)
            ?: throw BusinessException("Usuário e/ou senha inválidos")
    }
}