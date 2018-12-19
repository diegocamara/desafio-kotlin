package com.application

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.exposed.sql.Table
import java.time.LocalDate

data class User(
    var id: String? = null,
    var name: String?,
    var email: String?,
    @JsonIgnore var phones: List<Phone>? = null,
    var password: String?,
    var created: LocalDate? = null,
    var modified: LocalDate? = null,
    var lastLogin: LocalDate? = null,
    var token: String? = null
)

object Users : Table() {
    val id = varchar("id", 50).primaryKey()
    val name = varchar("name", 50)
    val email = varchar("email", 50)
    var password = varchar("password", 50)
    var created = datetime("created")
    var modified = datetime("modified")
    var lastLogin = datetime("lastLogin")
    var token = varchar("token", 255)
}



data class Phone(@JsonIgnore var id: Long? = null,
                 var number: String? = null,
                 var ddd: String? = null)

var users = hashMapOf(
    "uh7sf" to User(
        id = "uh7sf",
        name = "User1",
        email = "user1@email.com",
        phones = listOf(Phone(id = 1, number = "99999999", ddd = "999")),
        password = "123",
        created = LocalDate.now(),
        modified = LocalDate.now(),
        lastLogin = LocalDate.now()
    ),
    "uhsd897" to User(
        id = "uhsd897",
        name = "User2",
        email = "user2@email.com",
        phones = listOf(Phone(id = 2, number = "88888888", ddd = "888")),
        password = "123",
        created = LocalDate.now(),
        modified = LocalDate.now(),
        lastLogin = LocalDate.now()
    )
)