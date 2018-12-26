package com.application.service.impl

import com.application.config.exception.BusinessException
import com.application.domain.UserDTO
import com.application.dto.LoginDTO
import com.application.service.LoginService
import com.application.service.UserService
import org.eclipse.jetty.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

class LoginServiceImpl(private val userService: UserService, private val passwordEncoder: PasswordEncoder) :
    LoginService {

    override fun login(loginDTO: LoginDTO): UserDTO? {

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