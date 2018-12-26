package com.application.dao.impl

import com.application.dao.UserDAO
import com.application.domain.User
import com.application.domain.UserDTO
import com.application.domain.Users
import org.jetbrains.exposed.sql.select

class UserDAOImpl : UserDAO {

    override fun createUser(newUser: UserDTO): User {
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

    override fun countByEmail(email: String): Int {
        return Users.select { Users.email eq email }.count()
    }

    override fun findByEmail(userEmail: String?): User? {
        return User.find { (Users.email eq userEmail.toString()) }
            .firstOrNull()
    }

    override fun findById(id: Int): User? {
        return User.findById(id)
    }


}