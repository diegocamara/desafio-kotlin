package com.application.domain

import com.application.domain.Users.nullable
import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable

data class PhoneDTO(
    @JsonIgnore var id: Long? = null,
    var number: String? = null,
    var ddd: String? = null
)


class Phone(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Phone>(Phones)
    var ddd by Phones.ddd
    var email by Phones.number
    var user by User referencedOn Phones.user
}

object Phones : IntIdTable() {
    val ddd = varchar("ddd", 3)
    val number = varchar("number", 9)
    val user = reference("user", Users)
}

//object Users : IntIdTable() {
//    val name = Users.varchar("name", 50)
//    val email = Users.varchar("email", 50)
//    var password = Users.varchar("password", 50)
//    var created = Users.datetime("created").nullable()
//    var modified = Users.datetime("modified").nullable()
//    var lastLogin = Users.datetime("lastLogin").nullable()
//    var token = Users.varchar("token", 255).nullable()
//}

//class User(id: EntityID<Int>) : Entity<Int>(id) {
//    companion object : EntityClass<Int, User>(Users)
//    var name by Users.name
//    var email by Users.email
//    var password by Users.password
//    var created by Users.created
//    var modified by Users.modified
//    var lastLogin by Users.lastLogin
//    var token by Users.token
//}