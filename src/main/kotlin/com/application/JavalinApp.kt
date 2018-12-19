@file:JvmName("EntryPoint")
package com.application

import com.application.controller.UserController
import com.application.persistence.DatabaseFactory
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post

class JavalinApp(private val port: Int) {

    fun init(): Javalin {
        val app = Javalin.create().apply {
            port(port)
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        }.start()

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