package com.application.controller


import com.application.dao.UserDAO
import com.application.domain.User
import com.application.domain.UserDTO
import com.application.service.UserService
import io.javalin.Context

object UserController {

    private val userService: UserService = UserService(UserDAO())

    fun createUser(ctx: Context) {

        val user = ctx.validatedBody<UserDTO>().check({
            !it.name.isNullOrBlank()
        }, "Campo nome obrigatório").check({
            !it.email.isNullOrBlank()
        }, "Campo email obrigatório").check({
            !it.password.isNullOrBlank()
        }, "Campo password obrigatório").getOrThrow()

        val userCreated = userService.createUser(user)
        ctx.json(userCreated).status(201)
    }


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