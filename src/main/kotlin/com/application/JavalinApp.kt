@file:JvmName("EntryPoint")
package com.application

import com.application.config.exception.handler.ExceptionHandler
import com.application.config.mapper.configureMapper
import com.application.config.persistence.DatabaseFactory
import com.application.controller.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post

class JavalinApp(private val port: Int) {

    fun init(): Javalin {
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
                }
            }
        }

        return app
    }

}

fun main(args: Array<String>) {
    DatabaseFactory.init()
    JavalinApp(7000).init()
}