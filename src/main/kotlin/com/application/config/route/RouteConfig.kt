package com.application.config.route

import com.application.controller.LoginController
import com.application.controller.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder

class RouteConfig(private val userController: UserController, private val loginController: LoginController) {

    fun register(app: Javalin){

        app.get("/") { ctx -> ctx.json(mapOf("message" to " hora do show")) }

        app.routes {
            ApiBuilder.path("api") {
                ApiBuilder.path("users") {
                    ApiBuilder.post(userController::createUser)
                    ApiBuilder.path(":id") {
                        ApiBuilder.get(userController::findUserById)
                    }
                }
                ApiBuilder.path("login") {
                    ApiBuilder.post(loginController::login)
                }
            }
        }

    }

}