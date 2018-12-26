package com.application.service

import com.application.domain.UserDTO
import com.application.dto.NewUserDTO

interface UserService {

    fun createUser(newUserDTO: NewUserDTO): UserDTO

    fun findByEmail(email: String?, login: Boolean): UserDTO?

    fun findById(id: Int): UserDTO?

}