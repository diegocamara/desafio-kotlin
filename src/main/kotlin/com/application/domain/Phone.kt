package com.application.domain

import com.application.domain.Users.nullable
import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable

data class PhoneDTO(
    @JsonIgnore var id: Long? = null,
    var number: String,
    var ddd: String
)


class Phone(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Phone>(Phones)
    var ddd by Phones.ddd
    var number by Phones.number
    var user by User referencedOn Phones.user
}

object Phones : IntIdTable() {
    val ddd = varchar("ddd", 3)
    val number = varchar("number", 9)
    val user = reference("user", Users)
}