package com.application.service

import com.application.dao.PhoneDAO
import com.application.domain.Phone
import com.application.domain.PhoneDTO
import com.application.domain.User
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class PhoneService(private val phoneDAO: PhoneDAO) {

    fun createPhones(phones: List<PhoneDTO>, targetUser: User): List<Phone> {
        return transaction {
            addLogger(StdOutSqlLogger)
            phoneDAO.createPhones(phones, targetUser)
        }
    }

}