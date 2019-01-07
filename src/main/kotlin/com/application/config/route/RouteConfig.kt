package com.application.config.route

import com.application.controller.LoginController
import com.application.controller.UserController
import io.javalin.Javalin

class RouteConfig(private val userController: UserController, private val loginController: LoginController) {

    fun register(app: Javalin) {

        app.routes {
            userController.registerEndpoint()
            loginController.endpointGroup()
        }

    }

}