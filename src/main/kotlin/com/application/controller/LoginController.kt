package com.application.controller

import com.application.config.exception.BusinessException
import com.application.domain.UserDTO
import com.application.dto.LoginDTO
import com.application.service.LoginService
import io.javalin.Context
import io.javalin.apibuilder.ApiBuilder
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Api
@Path("Login")
@Produces("application/json")
@Tag(name = "Login")
class LoginController(private val loginService: LoginService) {

    @POST
    @Operation(
        summary = "Login"
    )
    fun login(loginDTO: LoginDTO): UserDTO? {
        validLoginFields(loginDTO)
        return loginService.login(loginDTO)
    }


    private fun validLoginFields(loginDTO: LoginDTO) {

        when {
            loginDTO.email.isNullOrEmpty() -> throw BusinessException("Campo email deve ser preenchido")
            loginDTO.password.isNullOrEmpty() -> throw BusinessException("Campo password deve ser preenchido")
        }


    }

    fun registerResources() {
        ApiBuilder.path("login") {
            ApiBuilder.post { ctx ->
                this.login(ctx.body<LoginDTO>())?.let { ctx.json(it) }
            }
        }
    }


}