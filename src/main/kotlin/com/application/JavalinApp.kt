@file:JvmName("EntryPoint")

package com.application

import com.application.config.exception.BusinessException
import com.application.config.exception.handler.ExceptionHandler
import com.application.config.mapper.configureMapper
import com.application.config.persistence.DatabaseFactory
import com.application.controller.LoginController
import com.application.controller.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import org.eclipse.jetty.http.HttpStatus

class JavalinApp(private val port: Int, private val createSchema: Boolean = false) {

    fun init(): Javalin {
        DatabaseFactory.init(createSchema)
        configureMapper()
        val app = Javalin.create().apply {
            port(port)
        }.start()

        ExceptionHandler.register(app)

        app.get("/") { ctx -> ctx.json(mapOf("message" to " hora do show")) }

        app.routes {
            path("api") {
                path("users") {
                    post(UserController::createUser)
                    path(":id") {
                        get(UserController::findUserById)
                    }
                }
                path("login") {
                    post(LoginController::login)
                }
            }
        }

        app.before("/api/users/:id") {
            it.header("Authorization") ?: throw BusinessException("Não autorizado", HttpStatus.UNAUTHORIZED_401)
        }

        return app
    }

}

fun main(args: Array<String>) {

    JavalinApp(7000).init()
}