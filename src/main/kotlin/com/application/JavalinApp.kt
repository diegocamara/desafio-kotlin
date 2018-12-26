@file:JvmName("EntryPoint")

package com.application

import com.application.config.exception.BusinessException
import com.application.config.exception.handler.ExceptionHandler
import com.application.config.koin.KoinModuleConfig
import com.application.config.mapper.configureMapper
import com.application.config.persistence.DatabaseFactory
import com.application.config.route.RouteConfig
import com.application.controller.LoginController
import com.application.controller.UserController
import io.javalin.Javalin
import io.javalin.JavalinEvent
import io.javalin.apibuilder.ApiBuilder.*
import org.eclipse.jetty.http.HttpStatus
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject

class JavalinApp(private val port: Int, private val createSchema: Boolean = false) : KoinComponent {

    private val routeConfig: RouteConfig by inject()

    fun init(): Javalin {
        DatabaseFactory.init(createSchema)
        configureMapper()
        val app = Javalin.create().apply {
            port(port)
        }.event(JavalinEvent.SERVER_STOPPING) { stopKoin() }.start()

        ExceptionHandler.register(app)

        routeConfig.register(app)

        app.before("/api/users/:id") {
            it.header("Authorization") ?: throw BusinessException("Não autorizado", HttpStatus.UNAUTHORIZED_401)
        }

        return app
    }

}

fun main(args: Array<String>) {
    startKoin(listOf(KoinModuleConfig.applicationModule))
    JavalinApp(7000, true).init()
}