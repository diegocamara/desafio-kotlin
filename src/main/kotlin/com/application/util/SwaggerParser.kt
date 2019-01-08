package com.application.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.Api
import io.swagger.v3.jaxrs2.Reader
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.integration.SwaggerConfiguration
import io.swagger.v3.oas.models.OpenAPI
import org.reflections.Reflections
import java.io.File

object SwaggerParser {

    fun generateDocs(packageName: String) {
        val openApi = getOpenAPI(packageName)
        writeApiToJson(openApi)
    }

    private fun getOpenAPI(packageName: String): OpenAPI {
        val reflections = Reflections(packageName)
        val apiClasses = reflections.getTypesAnnotatedWith(Api::class.java)
        apiClasses.add(reflections.getTypesAnnotatedWith(OpenAPIDefinition::class.java).first())
        val swaggerConfiguration =
            SwaggerConfiguration().resourcePackages(mutableSetOf(packageName)).prettyPrint(true)
        val reader = Reader(swaggerConfiguration)
        return reader.read(apiClasses)
    }

    private fun writeApiToJson(openAPI: OpenAPI) {
        val objectMapper = ObjectMapper()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        val jsonFile = File("src/main/resources/public/docs/swagger.json")
        jsonFile.createNewFile()
        objectMapper.writeValue(jsonFile, openAPI)
    }

}