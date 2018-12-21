package com.application.controller


import com.application.config.exception.BusinessException
import com.application.dao.PhoneDAO
import com.application.dao.UserDAO
import com.application.domain.UserDTO
import com.application.service.PhoneService
import com.application.service.UserService
import io.javalin.Context

object UserController {

    private val userService: UserService = UserService(UserDAO(), PhoneService(PhoneDAO()))

    fun createUser(ctx: Context) {
        val userDTO = ctx.body<UserDTO>()
        validUserFields(userDTO)
        val userCreated = userService.createUser(userDTO)
        ctx.json(userCreated).status(201)
    }

    private fun validUserFields(userDTO: UserDTO) {
        when {
            userDTO?.name.isNullOrEmpty() -> throw BusinessException("Campo nome obrigatório")
            userDTO?.email.isNullOrEmpty() -> throw BusinessException("Campo email obrigatório")
            userDTO?.password.isNullOrEmpty() -> throw BusinessException("Campo password obrigatório")
        }
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