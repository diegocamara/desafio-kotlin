package com.application.config.template

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.StringWriter
import java.util.*


object TemplateResolver {

    fun htmlTemplateResolver(): ClassLoaderTemplateResolver {
        val resolver = ClassLoaderTemplateResolver()
        resolver.prefix = "public/swagger-ui/"
        resolver.suffix = ".html"
        resolver.isCacheable = false
        resolver.templateMode = TemplateMode.HTML
        resolver.characterEncoding = "UTF-8"
        return resolver
    }

    fun context(): Context {
        val context = Context(Locale.getDefault())
        context.setVariable("swaggerJsonFileUrl", "http://localhost:8080/docs/swagger_${UUID.randomUUID()}.json")
        return context
    }

    fun templateEngine(): TemplateEngine {
        val engine = TemplateEngine()
        engine.addTemplateResolver(htmlTemplateResolver())
        return engine
    }

//    http://localhost:8080/docs/swagger.json

    fun process(templateSpec: String) {
        val stringWriter = StringWriter()
        templateEngine().process(templateSpec, context(), stringWriter)
        println(stringWriter.toString())
    }

}