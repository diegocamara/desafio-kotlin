package com.application.controller


import com.application.config.exception.BusinessException
import com.application.dto.NewUserDTO
import com.application.service.UserService
import io.javalin.Context
import org.eclipse.jetty.http.HttpStatus
import org.joda.time.DateTime
import org.joda.time.Minutes

class UserController(private val userService: UserService) {

    fun createUser(ctx: Context) {

        val newUserDTO = ctx.body<NewUserDTO>()
        validUserFields(newUserDTO)
        val userCreated = userService.createUser(newUserDTO)
        ctx.json(userCreated).status(201)

    }

    fun findUserById(ctx: Context) {

        val token = ctx.header("Authorization")?.replace("Bearer ", "")
        val userId = ctx.pathParam("id")
        val storedUser = userService.findById(userId.toInt())

        if (!token.equals(storedUser?.token)) {
            throw BusinessException("Não autorizado", HttpStatus.UNAUTHORIZED_401)
        }

        val time = Minutes.minutesBetween(storedUser?.lastLogin, DateTime.now()).minutes

        if (time > 30) {
            throw BusinessException("Sessão inválida", HttpStatus.UNAUTHORIZED_401)
        }

        if (storedUser != null) {
            ctx.json(storedUser).status(HttpStatus.OK_200)
        }

    }

    private fun validUserFields(newUserDTO: NewUserDTO) {
        when {
            newUserDTO.name.isNullOrEmpty() -> throw BusinessException("Campo nome obrigatório")
            newUserDTO.email.isNullOrEmpty() -> throw BusinessException("Campo email obrigatório")
            newUserDTO.password.isNullOrEmpty() -> throw BusinessException("Campo password obrigatório")
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