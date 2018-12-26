package com.application.dao

import com.application.domain.User
import com.application.domain.UserDTO

interface UserDAO {

    fun createUser(newUser: UserDTO): User

    fun countByEmail(email: String): Int

    fun findByEmail(userEmail: String?): User?

    fun findById(id: Int): User?

}