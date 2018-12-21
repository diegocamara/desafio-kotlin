package com.application.dao

import com.application.domain.User
import com.application.domain.UserDTO
import com.application.domain.Users
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserDAO {

    fun createUser(newUser: UserDTO): User {
        return User.new {
            name = newUser.name.toString()
            email = newUser.email.toString()
            password = newUser.password.toString()
            created = newUser.created
            modified = newUser.modified
            lastLogin = newUser.lastLogin
            token = newUser.token
        }

    }

    fun countByEmail(email: String): Int {
        return Users.select { Users.email eq email }.count()
    }

    fun findByEmailAndPassword(userEmail: String?, userPassword: String?): User {
       return User.find { (Users.email eq userEmail.toString()).and(Users.password eq userPassword.toString()) }.first()
    }


}