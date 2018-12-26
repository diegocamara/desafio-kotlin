package com.application.service

import com.application.domain.UserDTO
import com.application.dto.LoginDTO

interface LoginService {

    fun login(loginDTO: LoginDTO): UserDTO?

}