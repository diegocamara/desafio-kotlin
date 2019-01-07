package com.application.controller


import com.application.config.exception.BusinessException
import com.application.controller.UserController.Companion.USERS_PATH
import com.application.domain.UserDTO
import com.application.dto.NewUserDTO
import com.application.service.UserService
import io.javalin.Context
import io.javalin.Handler
import io.javalin.apibuilder.ApiBuilder
import io.swagger.annotations.*
import org.eclipse.jetty.http.HttpStatus
import org.joda.time.DateTime
import org.joda.time.Minutes
import javax.ws.rs.*

@Api
@Path(USERS_PATH)
@Produces("application/json")
class UserController(private val userService: UserService) {

    companion object {
        const val USERS_PATH = "/users"
        const val USER_CREATE_STATUS_CODE = HttpStatus.CREATED_201
    }

    @POST
    @ApiOperation(value = "Creates a new user")
    @ApiResponses(value = [ApiResponse(code = HttpStatus.CREATED_201, message = "Success!")])
    fun createUser(newUserDTO: NewUserDTO): UserDTO {
        validUserFields(newUserDTO)
        return userService.createUser(newUserDTO)
    }

    @GET
    @ApiOperation(value = "Find a user")
    @ApiResponses(
        value = [ApiResponse(
            code = HttpStatus.OK_200,
            message = "Success!"
        ), ApiResponse(code = HttpStatus.UNAUTHORIZED_401, message = "Success!")]
    )
    fun findUserById(token: String, id: String): UserDTO {

//        val token = ctx.header("Authorization")?.replace("Bearer ", "")
//        val userId = ctx.pathParam("id")
//
//        val storedUser = userService.findById(userId.toInt())
//
//        if (!token.equals(storedUser?.token)) {
//            throw BusinessException("Não autorizado", HttpStatus.UNAUTHORIZED_401)
//        }
//
//        val time = Minutes.minutesBetween(storedUser?.lastLogin, DateTime.now()).minutes
//
//        if (time > 30) {
//            throw BusinessException("Sessão inválida", HttpStatus.UNAUTHORIZED_401)
//        }
//
//        if (storedUser != null) {
//            ctx.json(storedUser).status(HttpStatus.OK_200)
//        }
        return UserDTO()
    }

    private fun validUserFields(newUserDTO: NewUserDTO) {
        when {
            newUserDTO.name.isNullOrEmpty() -> throw BusinessException("Campo nome obrigatório")
            newUserDTO.email.isNullOrEmpty() -> throw BusinessException("Campo email obrigatório")
            newUserDTO.password.isNullOrEmpty() -> throw BusinessException("Campo password obrigatório")
        }
    }

    fun registerEndpoint() {
        ApiBuilder.path("api") {
            ApiBuilder.path(USERS_PATH) {


                ApiBuilder.post { ctx ->
                    ctx.json(this.createUser(ctx.body<NewUserDTO>())).status(USER_CREATE_STATUS_CODE)
                }





                ApiBuilder.path(":id") {
                    //                    ApiBuilder.get(this::findUserById)
                }
            }
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