package com.application.service

import com.application.config.exception.BusinessException
import com.application.domain.UserDTO
import com.application.dto.LoginDTO
import org.eclipse.jetty.http.HttpStatus
import org.joda.time.DateTime
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class LoginService(private val userService: UserService) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun login(loginDTO: LoginDTO): UserDTO? {

        val userDTO: UserDTO? =
            userService.findByEmail(email = loginDTO.email, login = true) ?: throw BusinessException(
                "Usuário e/ou senha inválidos",
                HttpStatus.NOT_FOUND_404
            )



        if (!passwordEncoder.matches(loginDTO?.password, userDTO?.password)) {
            throw BusinessException("Usuário e/ou senha inválidos", HttpStatus.UNAUTHORIZED_401)
        }


        return userDTO
    }
}