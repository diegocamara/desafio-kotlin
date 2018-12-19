package com.application.controller

import com.application.User
import io.javalin.Context

class UserController(private val data: Map<Int, User>) {

    fun getUser(ctx: Context) {
        ctx.pathParam("id").toInt().let {
            data[it]?.let { user ->
                ctx.json(user)
                return
            }
            ctx.status(404)
        }
    }

}