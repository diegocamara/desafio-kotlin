package com.application

import com.application.JavalinApp
import com.application.util.toJsonObject
import io.javalin.Javalin
import junit.framework.TestCase

class IntegrationTest : TestCase() {

    private lateinit var app: Javalin
    private val url = "http://localhost:7000"

    override fun setUp(){
        app = JavalinApp(port = 7000).init()
    }

    override fun tearDown(){
        app.stop()
    }

    fun `testar se o root da api responde 200` () {
        val response = khttp.get(url = url)
        assertEquals(200, response.statusCode)
    }

    fun `testar se user id 0 responde 200` () {
        val response = khttp.get(url = "$url/api/users/0")
        assertEquals(200, response.statusCode)
        var user = response.text.toJsonObject(User::class.java)
        assertEquals("User1", user.nome)
    }

    fun `testar se user id 1 responde 200 e nome e Caio`() {
        val response = khttp.get(url = "$url/api/users/1")
        assertEquals(200, response.statusCode)
        assertEquals("User2", response.text.toJsonObject(User::class.java).nome)
    }

    fun `testar se user id -1 responde 404`() {
        val response = khttp.get(url = "$url/api/users/-1")
        assertEquals(404, response.statusCode)
    }

}