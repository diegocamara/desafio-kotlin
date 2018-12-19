package com.application.controller

import com.application.User
import com.application.users
import io.javalin.Context
import java.time.LocalDate
import java.util.*

object UserController {

    fun createUser(ctx: Context){
        val userCreated = ctx.body<User>()
        userCreated.id = randomId()
        val createDate = LocalDate.now()
        userCreated.created = createDate
        userCreated.modified = createDate
        userCreated.lastLogin = createDate
        users[userCreated.id.toString()] = userCreated
        ctx.json(userCreated).status(201)
    }

    private fun randomId() = UUID.randomUUID().toString()

}

//class UserController(private val data: Map<Int, User>) {
//
//    fun getUser(ctx: Context) {
//        ctx.pathParam("id").toInt().let {
//            data[it]?.let { user ->
//                ctx.json(user)
//                return
//            }
//            ctx.status(404)
//        }
//    }
//
//}