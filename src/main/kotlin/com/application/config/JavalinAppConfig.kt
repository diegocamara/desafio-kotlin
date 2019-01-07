@file:JvmName("EntryPoint")

package com.application.config

import com.application.config.handler.before.BeforeHandler
import com.application.config.handler.exception.ExceptionHandler
import com.application.config.koin.KoinModuleConfig
import com.application.config.mapper.configureMapper
import com.application.config.persistence.DatabaseFactory
import com.application.config.route.RouteConfig
import com.application.constants.ApplicationConstants
import com.application.util.SwaggerParser
import io.javalin.Javalin
import io.javalin.JavalinEvent
import io.swagger.annotations.SwaggerDefinition
import org.koin.core.KoinProperties
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.getProperty
import org.koin.standalone.inject

@SwaggerDefinition(
    host = "localhost:4567", //
    schemes = [SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS], //
    consumes = ["application/json"], //
    produces = ["application/json"]
)
class JavalinAppConfig(private val createSchema: Boolean = false) : KoinComponent {

    private val routeConfig: RouteConfig by inject()

    fun init(): Javalin {

        startKoin(
            listOf(KoinModuleConfig.applicationModule),
            KoinProperties(useEnvironmentProperties = true, useKoinPropertiesFile = true)
        )

        DatabaseFactory.init(createSchema)

        configureMapper()

        val app = Javalin.create().apply {
            port(getProperty(ApplicationConstants.SERVER_PORT, ApplicationConstants.DEFAULT_SERVER_PORT))
        }.event(JavalinEvent.SERVER_STOPPING) {
            stopKoin()
            DatabaseFactory.drop()
        }.start()

        ExceptionHandler.register(app)
        BeforeHandler.register(app)
        routeConfig.register(app)

        app.handlerMetaInfo.forEach{

            println(it)

        }

        val swaggerJson = SwaggerParser.getSwaggerJson("com.application")
        println(swaggerJson)

        return app
    }

}

fun main(args: Array<String>) {
    JavalinAppConfig(true).init()
}