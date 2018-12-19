package com.application

import com.application.controller.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path

class JavalinApp(private val port: Int) {

    val controller = UserController(users)

    fun init(): Javalin {
        val app = Javalin.create().apply {
            port(port)
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        }.start()

        app.get("/") { ctx -> ctx.json(mapOf("message" to " hora do show")) }

        app.routes {
            path("api") {
                path("users") {
                    path(":id") {
                        get {
                            ctx -> controller.getUser(ctx)
                        }
                    }
                }
            }
        }

        return app
    }


}

fun main(args: Array<String>) {
    JavalinApp(7000).init()
}