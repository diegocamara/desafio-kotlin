package com.application.service

import com.application.dao.UserDAO
import com.application.domain.User
import com.application.domain.UserDTO
import org.joda.time.DateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class UserService(private val userDAO: UserDAO) {

    fun createUser(newUser: UserDTO): UserDTO {
        val createDate = LocalDateTime.now()
        newUser.created = createDate
        newUser.modified = createDate
        newUser.lastLogin = createDate
        newUser.token = randomId()
        return  userDAO.createUser(newUser)
    }

    private fun randomId() = UUID.randomUUID().toString()

}