package com.application.domain

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime
import java.time.LocalDate
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer
import java.time.LocalDateTime


@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDTO(
    var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var phones: List<PhoneDTO>? = listOf(),
    var password: String? = null,
    var created: DateTime? = null,
    var modified: DateTime? = null,
    var token: String? = null,
    @JsonProperty(value = "last_login") var lastLogin: DateTime? = null
)

class User(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, User>(Users)
    var name by Users.name
    var email by Users.email
    var password by Users.password
    var created by Users.created
    var modified by Users.modified
    var lastLogin by Users.lastLogin
    var token by Users.token
    val phones by Phone referrersOn Phones.user
}

object Users : IntIdTable() {
    val name = Users.varchar("name", 50)
    val email = Users.varchar("email", 50)
    var password = Users.varchar("password", 50)
    var created = Users.datetime("created").nullable()
    var modified = Users.datetime("modified").nullable()
    var lastLogin = Users.datetime("lastLogin").nullable()
    var token = Users.varchar("token", 255).nullable()
}