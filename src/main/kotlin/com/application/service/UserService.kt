package com.application.service

import com.application.config.exception.BusinessException
import com.application.dao.UserDAO
import com.application.domain.User
import com.application.domain.UserDTO
import com.application.dto.NewUserDTO
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class UserService(private val userDAO: UserDAO, private val phoneService: PhoneService) {

    fun createUser(newUserDTO: NewUserDTO): UserDTO {

        validUserEmail(newUserDTO)

        val newUser = UserDTO(
            name = newUserDTO.name,
            email = newUserDTO.email,
            password = newUserDTO.password,
            phones = newUserDTO.phones
        )

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

    private fun validUserEmail(newUserDTO: NewUserDTO) {
        if (existsUser(newUserDTO.email.toString())) {
            throw BusinessException("E-mail já existente")
        }
    }

    fun existsUser(email: String): Boolean {
        return transaction {
            addLogger(StdOutSqlLogger)
            userDAO.countByEmail(email) > 0
        }
    }

    fun findByEmail(email: String?): UserDTO? {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user: User? = userDAO.findByEmail(email)
            if (user != null) toUserDTO(user) else null
        }
    }

    fun findById(id: Int): UserDTO? {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user: User? = userDAO.findById(id)
            if (user != null) toUserDTO(user) else null
        }
    }

    private fun randomId() = UUID.randomUUID().toString()

    private fun toUserDTO(user: User?): UserDTO {
        return UserDTO(
            id = user?.id?.value,
            name = user?.name,
            email = user?.email,
            password = user?.password,
            created = user?.created,
            modified = user?.modified,
            lastLogin = user?.lastLogin,
            token = user?.token
        )
    }


}