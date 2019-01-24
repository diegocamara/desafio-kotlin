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
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.javalin.Context
import io.javalin.Handler
import io.javalin.Javalin
import io.javalin.JavalinEvent
import io.javalin.apibuilder.ApiBuilder
import io.javalin.core.util.OptionalDependency
import io.javalin.core.util.Util
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.OpenAPI
import org.apache.http.entity.ContentType
import org.koin.core.KoinProperties
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.getProperty
import org.koin.standalone.inject

@OpenAPIDefinition(
    info = Info(title = "SuperAPI", version = "1.0.0"),
    servers = [Server(url = "http://localhost:8080/api")]
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
        }.swagger(packageToScan = "com.application").start()

        ExceptionHandler.register(app)
        BeforeHandler.register(app)
        routeConfig.register(app)

        return app

    }

}

fun main(args: Array<String>) {
    JavalinAppConfig(true).init()
}


// Swagger extension for Javalin

fun Javalin.swagger(
    enable: Boolean = true,
    enableUiEndpoint: Boolean = true,
    enableJsonEndpoint: Boolean = true,
    packageToScan: String = "*",
    apiFileName: String = "openapi.json",
    swaggerUIendPoint: String = "swagger-ui"
): Javalin {

    if (enable) {

        this.enableWebJars()

        val openApi = SwaggerParser.scan(packageToScan)

        this.routes {

            if (enableJsonEndpoint) {
                ApiBuilder.get(apiFileName) {
                    it.result(openApiToJson(openApi)).contentType(ContentType.APPLICATION_JSON.mimeType)
                }
            }

            if (enableUiEndpoint) {
                ApiBuilder.get(swaggerUIendPoint, SuperSwaggerRenderer("/$apiFileName"))
            }

        }

    }

    return this
}

fun openApiToJson(openAPI: OpenAPI): String {
    val objectMapper = ObjectMapper()
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
    return objectMapper.writeValueAsString(openAPI)
}

class SuperSwaggerRenderer(private val specUrl: String) : Handler {

    private val swaggerVersion = OptionalDependency.SWAGGERUI.version

    override fun handle(ctx: Context) {
        if (ctx.queryParam("spec") != null)
            ctx.result(Util.getResource(ctx.queryParam("spec")!!)!!.readText())
        else ctx.html(
            """
            <head>
                <meta charset="UTF-8">
                <title>Swagger UI</title>
                <link rel="icon" type="image/png" href="/webjars/swagger-ui/$swaggerVersion/favicon-16x16.png" sizes="16x16" />
                <link rel="stylesheet" href="/webjars/swagger-ui/$swaggerVersion/swagger-ui.css" >
                <script src="/webjars/swagger-ui/$swaggerVersion/swagger-ui-bundle.js"></script>
                <style>body{background:#fafafa;}</style>
            </head>
            <body>
                <div id="swagger-ui"></div>
                <script>
                    window.ui = SwaggerUIBundle({
                        url: "$specUrl",
                        dom_id: "#swagger-ui",
                        deepLinking: true,
                        presets: [SwaggerUIBundle.presets.apis],
                    });
                </script>
            </body>""".trimIndent()
        )
    }

}