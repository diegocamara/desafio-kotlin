package com.application.config.route

import com.application.controller.LoginController
import com.application.controller.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder

class RouteConfig(private val userController: UserController, private val loginController: LoginController) {

    fun register(app: Javalin) {

        app.routes {
            ApiBuilder.path("api") {
                userController.registerResources()
                loginController.registerResources()
            }
//            ApiBuilder.get("apidocs", SwaggerRenderer("public/${SwaggerParser.scan("com.application")}"))
        }

    }

}