package com.application.controller


import com.application.config.exception.BusinessException
import com.application.controller.UserController.Companion.USERS_PATH
import com.application.domain.UserDTO
import com.application.dto.NewUserDTO
import com.application.service.UserService
import io.javalin.apibuilder.ApiBuilder
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.tags.Tag
import org.eclipse.jetty.http.HttpStatus
import org.joda.time.DateTime
import org.joda.time.Minutes
import javax.ws.rs.*
import javax.ws.rs.core.HttpHeaders

@Api
@Path(USERS_PATH)
@Produces("application/json")
@Tag(name = "Users API v1.0.0-SHOWTIME")
@SecurityScheme(
    name = "jwt",
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.HEADER,
    paramName = "Authorization"
)
class UserController(private val userService: UserService) {

    companion object {
        const val USERS_PATH = "/users"
    }

    @POST
    @Operation(
        summary = "Creates a new user"
    )
    fun createUser(newUserDTO: NewUserDTO): UserDTO {
        validUserFields(newUserDTO)
        return userService.createUser(newUserDTO)
    }

    @GET
    @Path("/{id}")
    @Operation(
        summary = "Find a user"
    )
    @SecurityRequirement(name = "jwt")
    fun findUserById(token: String, @PathParam(value = "id") userId: String): UserDTO {

        val storedUser = userService.findById(userId.toInt())

        if (token != storedUser?.token) {
            throw BusinessException("Não autorizado", HttpStatus.UNAUTHORIZED_401)
        }

        val time = Minutes.minutesBetween(storedUser.lastLogin, DateTime.now()).minutes

        if (time > 30) {
            throw BusinessException("Sessão inválida", HttpStatus.UNAUTHORIZED_401)
        }

        return storedUser
    }

    private fun validUserFields(newUserDTO: NewUserDTO) {
        when {
            newUserDTO.name.isNullOrEmpty() -> throw BusinessException("Campo nome obrigatório")
            newUserDTO.email.isNullOrEmpty() -> throw BusinessException("Campo email obrigatório")
            newUserDTO.password.isNullOrEmpty() -> throw BusinessException("Campo password obrigatório")
        }
    }

    fun registerResources() {
        ApiBuilder.path(USERS_PATH) {
            ApiBuilder.post { ctx ->
                ctx.json(this.createUser(ctx.body<NewUserDTO>())).status(HttpStatus.CREATED_201)
            }
            ApiBuilder.path(":id") {
                ApiBuilder.get { ctx ->
                    val token = ctx.header("Authorization")?.replace("Bearer ", "")
                    val userId = ctx.pathParam("id")
                    ctx.json(this.findUserById(token.toString(), userId)).status(HttpStatus.OK_200)
                }
            }
        }
    }
}