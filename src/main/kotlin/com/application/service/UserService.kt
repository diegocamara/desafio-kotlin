package com.application.service

import com.application.config.exception.BusinessException
import com.application.dao.UserDAO
import com.application.domain.User
import com.application.domain.UserDTO
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class UserService(private val userDAO: UserDAO, private val phoneService: PhoneService) {

    fun createUser(newUser: UserDTO): UserDTO {

        validUserEmail(newUser)

        val createDate = DateTime.now()
        newUser.created = createDate
        newUser.modified = createDate
        newUser.lastLogin = createDate
        newUser.token = randomId()

        val storedUser = transaction {
            addLogger(StdOutSqlLogger)
            userDAO.createUser(newUser)
        }

        newUser.phones.let {
            val storedPhones = phoneService.createPhones(it!!, storedUser)
        }

        return toUserDTO(storedUser)
    }

    private fun validUserEmail(userDTO: UserDTO) {
        if (existsUser(userDTO.email.toString())) {
            throw BusinessException("E-mail já existente")
        }
    }

    fun existsUser(email: String): Boolean {
        return transaction {
            addLogger(StdOutSqlLogger)
            userDAO.countByEmail(email) > 0
        }
    }

    fun findByEmailAndPassword(email: String?, password: String?): UserDTO? {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user: User = userDAO.findByEmailAndPassword(email, password)
            toUserDTO(user)
        }
    }

    private fun randomId() = UUID.randomUUID().toString()

    private fun toUserDTO(user: User): UserDTO {
        return UserDTO(
            id = user.id.value,
            name = user.name,
            email = user.email,
            created = user.created,
            modified = user.modified,
            lastLogin = user.lastLogin,
            token = user.token
        )
    }


}