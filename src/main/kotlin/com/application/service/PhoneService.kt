package com.application.service

import com.application.domain.Phone
import com.application.domain.PhoneDTO
import com.application.domain.User

interface PhoneService {

    fun createPhones(phones: List<PhoneDTO>, targetUser: User): List<Phone>

}