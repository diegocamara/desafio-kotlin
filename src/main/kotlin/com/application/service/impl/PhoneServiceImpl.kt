package com.application.service.impl

import com.application.dao.PhoneDAO
import com.application.dao.impl.PhoneDAOImpl
import com.application.domain.Phone
import com.application.domain.PhoneDTO
import com.application.domain.User
import com.application.service.PhoneService
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class PhoneServiceImpl(private val phoneDAO: PhoneDAO) : PhoneService {

    override fun createPhones(phones: List<PhoneDTO>, targetUser: User): List<Phone> {
        return transaction {
            addLogger(StdOutSqlLogger)
            phoneDAO.createPhones(phones, targetUser)
        }
    }

}