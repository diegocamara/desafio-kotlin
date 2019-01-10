package com.application.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.swagger.annotations.Api
import io.swagger.v3.jaxrs2.Reader
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.integration.SwaggerConfiguration
import io.swagger.v3.oas.models.OpenAPI
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.RandomStringUtils
import org.reflections.Reflections
import java.io.File

object SwaggerParser {

    fun generateDocs(packageName: String): String {
        val openApi = getOpenAPI(packageName)
        return writeApiToJsonFile(openApi)
    }

    private fun getOpenAPI(packageName: String): OpenAPI {
        val reflections = Reflections(packageName)
        val apiClasses = reflections.getTypesAnnotatedWith(Api::class.java)
        apiClasses.add(reflections.getTypesAnnotatedWith(OpenAPIDefinition::class.java).first())
        val swaggerConfiguration =
            SwaggerConfiguration().resourcePackages(mutableSetOf(packageName)).readAllResources(true)
        val reader = Reader(swaggerConfiguration)
        return reader.read(apiClasses)
    }

    private fun writeApiToJsonFile(openAPI: OpenAPI): String {
        val objectMapper = ObjectMapper()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
        val fileName = "swagger_version-${RandomStringUtils.randomAlphanumeric(10)}.json"
        val docsDirectory = File("${SwaggerParser::class.java.classLoader.getResource("public/docs/").path}")

        FileUtils.cleanDirectory(docsDirectory)

        val jsonFile = File("${docsDirectory.path}/$fileName")
        jsonFile.createNewFile()
        
        objectMapper.writeValue(jsonFile, openAPI)
        return fileName
    }

}