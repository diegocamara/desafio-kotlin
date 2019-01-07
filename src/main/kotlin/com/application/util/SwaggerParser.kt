package com.application.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.Api
import io.swagger.jaxrs.Reader
import io.swagger.jaxrs.config.BeanConfig
import io.swagger.models.Swagger
import org.reflections.Reflections

object SwaggerParser {

    @Throws(JsonProcessingException::class)
    fun getSwaggerJson(packageName: String): String {
        val swagger = getSwagger(packageName)
        return swaggerToJson(swagger)
    }

    fun getSwagger(packageName: String): Swagger {
        val reflections = Reflections(packageName)
        val beanConfig = BeanConfig()
        beanConfig.resourcePackage = packageName
        beanConfig.scan = true
        beanConfig.scanAndRead()
        val swagger = beanConfig.swagger

        val reader = Reader(swagger)

        val apiClasses = reflections.getTypesAnnotatedWith(Api::class.java)
        return reader.read(apiClasses)
    }

    @Throws(JsonProcessingException::class)
    fun swaggerToJson(swagger: Swagger): String {
        val objectMapper = ObjectMapper()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        return objectMapper.writeValueAsString(swagger)
    }

}