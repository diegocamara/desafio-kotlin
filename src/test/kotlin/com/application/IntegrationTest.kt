package com.application

import com.application.config.persistence.DatabaseFactory
import com.application.domain.UserDTO
import com.application.config.exception.response.BadRequestErrorResponse
import com.application.domain.PhoneDTO
import com.application.util.toJsonObject
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import junit.framework.TestCase

class RestIntegrationTest : TestCase() {

    private lateinit var app: Javalin
    private val url = "http://localhost:7000"

    
    override fun setUp() {
        DatabaseFactory.init(createSchema = true)
        app = JavalinApp(port = 7000).init()
    }

    override fun tearDown() {
        app.stop()
    }

    fun `testar se o usuário foi criado 201`() {
        val phones = listOf(PhoneDTO(ddd = "99", number = "99999999"))
        val user = UserDTO(name = "User", email = "user@email.com", password = "showtime123", phones = phones)
        val jsonBody = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val response = khttp.post(url = "$url/api/users", data = jsonBody)
        assertEquals(201, response.statusCode)
    }

    fun `testar se os campos obrigatórios estão sendo retornados`() {
        val user = UserDTO(name = "Jaspion", email = "jaspion@email.com", password = "showtime123")
        val jsonBody = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val response = khttp.post(url = "$url/api/users", data = jsonBody)
        val userCreated = response.text.toJsonObject(UserDTO::class.java)
        assertNotNull(userCreated.id)
        assertNotNull(userCreated.created)
        assertNotNull(userCreated.modified)
        assertNotNull(userCreated.lastLogin)
        assertNotNull(userCreated.token)
    }

    fun `testar validar campo nome não preenchido`(){
        val user = UserDTO(email = "ironuser@mail.com", password = "iron123")
        val jsonBody = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val response = khttp.post(url = "$url/api/users", data = jsonBody)
        val message = response.text.toJsonObject(BadRequestErrorResponse::class.java)
        assertEquals("Request body as UserDTO invalid - Campo nome obrigatório", message.message)
    }

    fun `testar validar campo email não preenchido`(){
        val user = UserDTO(name = "user", password = "iron123")
        val jsonBody = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val response = khttp.post(url = "$url/api/users", data = jsonBody)
        val message = response.text.toJsonObject(BadRequestErrorResponse::class.java)
        assertEquals("Request body as UserDTO invalid - Campo email obrigatório", message.message)
    }

    fun `testar validar campo password não preenchido`(){
        val user = UserDTO(name = "user", email = "user@mail.com")
        val jsonBody = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val response = khttp.post(url = "$url/api/users", data = jsonBody)
        val message = response.text.toJsonObject(BadRequestErrorResponse::class.java)
        assertEquals("Request body as UserDTO invalid - Campo password obrigatório", message.message)
    }



}

