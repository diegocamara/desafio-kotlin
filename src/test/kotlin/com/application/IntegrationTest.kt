package com.application

import com.application.config.persistence.DatabaseFactory
import com.application.domain.UserDTO
import com.application.util.toJsonObject
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import junit.framework.TestCase

class IntegrationTest : TestCase() {

    private lateinit var app: Javalin
    private val url = "http://localhost:7000"

    override fun setUp() {
        DatabaseFactory.init()
        app = JavalinApp(port = 7000).init()
    }

    override fun tearDown() {
        app.stop()
    }

    fun `testar se o usuário foi criado 201`() {
        val user = UserDTO(name = "User", email = "user@email.com", password = "showtime123")
        val json = jacksonObjectMapper().writeValueAsString(user)
        var response = khttp.post(url = "$url/api/users", data = json)
        assertEquals(201, response.statusCode)
    }

    fun `testar se os campos obrigatórios estão sendo retornados`() {
        val user = UserDTO(name = "Jaspion", email = "jaspion@email.com", password = "showtime123")
        val json = jacksonObjectMapper().writeValueAsString(user)
        var response = khttp.post(url = "$url/api/users", data = json)
        val userCreated = response.text.toJsonObject(UserDTO::class.java)
        assertNotNull(userCreated.id)
        assertNotNull(userCreated.token)
    }

}

