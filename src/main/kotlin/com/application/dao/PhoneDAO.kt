package com.application.dao

import com.application.domain.Phone
import com.application.domain.PhoneDTO
import com.application.domain.User

interface PhoneDAO {

    fun createPhones(phones: List<PhoneDTO>, targetUser: User): List<Phone>

}