package com.application.service.impl

import com.application.config.exception.BusinessException
import com.application.dao.UserDAO
import com.application.domain.PhoneDTO
import com.application.domain.User
import com.application.domain.UserDTO
import com.application.dto.NewUserDTO
import com.application.service.PhoneService
import com.application.service.UserService
import com.application.util.JwtUtil
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceImpl(
    private val userDAO: UserDAO,
    private val phoneService: PhoneService,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun createUser(newUserDTO: NewUserDTO): UserDTO {

        validUserEmail(newUserDTO)

        val newUser = UserDTO(
            name = newUserDTO.name,
            email = newUserDTO.email?.trim(),
            password = passwordEncoder.encode(newUserDTO.password),
            phones = newUserDTO.phones
        )

        val createDate = DateTime.now()
        newUser.created = createDate
        newUser.modified = createDate
        newUser.lastLogin = createDate

        val storedUser = transaction {
            addLogger(StdOutSqlLogger)
            val user = userDAO.createUser(newUser)
            user.token = JwtUtil.sign(UserDTO(id = user.id.value), maxAgeInMinutes = 30)
            user

        }

        newUser.phones.let {
            val storedPhones = phoneService.createPhones(it!!, storedUser)
        }

        return toUserDTO(storedUser)
    }

    private fun validUserEmail(newUserDTO: NewUserDTO) {
        if (existsUser(newUserDTO.email?.trim().toString())) {
            throw BusinessException("E-mail já existente")
        }
    }

    private fun existsUser(email: String): Boolean {
        return transaction {
            addLogger(StdOutSqlLogger)
            userDAO.countByEmail(email) > 0
        }
    }

    override fun findByEmail(email: String?, login: Boolean): UserDTO? {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user: User? = userDAO.findByEmail(email)

            if (login) {
                user?.lastLogin = DateTime.now()
                user?.token = JwtUtil.sign(UserDTO(id = user?.id?.value), maxAgeInMinutes = 30)
            }

            if (user != null) toUserDTO(user) else null
        }
    }

    override fun findById(id: Int): UserDTO? {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user: User? = userDAO.findById(id)
            if (user != null) toUserDTO(user) else null
        }
    }

    private fun toUserDTO(user: User?): UserDTO {

        val phones = transaction { user?.phones?.map { phone -> PhoneDTO(ddd = phone.ddd, number = phone.number) } }

        return UserDTO(
            id = user?.id?.value,
            name = user?.name,
            email = user?.email,
            password = user?.password,
            created = user?.created,
            modified = user?.modified,
            lastLogin = user?.lastLogin,
            token = user?.token,
            phones = phones

        )
    }


}