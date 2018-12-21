package com.application.dao

import com.application.domain.User
import com.application.domain.UserDTO
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class UserDAO {

    fun createUser(newUser: UserDTO): UserDTO {
        return transaction {
            addLogger(StdOutSqlLogger)
            val storedUser = User.new {
                name = newUser.name.toString()
                email = newUser.email.toString()
                password = newUser.password.toString()
                created = newUser.created
                modified = newUser.modified
                lastLogin = newUser.lastLogin
                token = newUser.token
            }
            UserDTO(
                id = storedUser.id.value,
                name = storedUser.name,
                email = storedUser.email,
                created = storedUser.created,
                modified = storedUser.modified,
                lastLogin = storedUser.lastLogin,
                token = storedUser.token
            )
        }

    }

}