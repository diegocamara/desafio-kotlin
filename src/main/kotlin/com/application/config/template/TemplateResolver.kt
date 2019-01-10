package com.application.config.template

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File
import java.io.FileWriter
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Paths
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

    fun context(docFileName: String): Context {
        val context = Context(Locale.getDefault())
        context.setVariable("swaggerJsonFileUrl", "http://localhost:8080/docs/$docFileName")
        return context
    }

    fun templateEngine(): TemplateEngine {
        val engine = TemplateEngine()
        engine.addTemplateResolver(htmlTemplateResolver())
        return engine
    }

    fun process(templateSpec: String, docFileName: String) {
        val indexPath = "${TemplateResolver::class.java.classLoader.getResource("public/swagger-ui/").path}/index.html"
        val fileWriter = FileWriter(indexPath)
        templateEngine().process(templateSpec, context(docFileName), fileWriter)
    }

}