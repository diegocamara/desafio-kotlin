package com.application.dao.impl

import com.application.dao.PhoneDAO
import com.application.domain.Phone
import com.application.domain.PhoneDTO
import com.application.domain.User

class PhoneDAOImpl : PhoneDAO {

   override fun createPhones(phones: List<PhoneDTO>, targetUser: User): List<Phone> {
        return phones.map { phoneDTO ->
            Phone.new {
                ddd = phoneDTO.ddd
                number = phoneDTO.number
                user = targetUser
            }
        }
    }


}