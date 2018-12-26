package com.application

import com.application.authorization.BearerTokenAuthorization
import com.application.config.exception.response.ApplicationErrorResponse
import com.application.config.koin.KoinModuleConfig
import com.application.config.persistence.DatabaseFactory
import com.application.domain.PhoneDTO
import com.application.domain.UserDTO
import com.application.dto.NewUserDTO
import com.application.dto.LoginDTO
import com.application.util.toJsonObject
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import junit.framework.TestCase
import org.eclipse.jetty.http.HttpStatus
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.koin.standalone.StandAloneContext

class RestIntegrationTest : TestCase() {

    private lateinit var app: Javalin
    private val url = "http://localhost:7000"

    override fun setUp() {
        StandAloneContext.startKoin(listOf(KoinModuleConfig.applicationModule))
        app = JavalinApp(port = 7000, createSchema = true).init()
    }

    override fun tearDown() {
        app.stop()
        DatabaseFactory.drop()
        DateTimeUtils.setCurrentMillisSystem()
    }

    fun `testar se o usuário foi criado 201`() {
        val phones = listOf(PhoneDTO(ddd = "99", number = "99999999"))
        val user = NewUserDTO(name = "User", email = "user@email.com", password = "showtime123", phones = phones)
        val jsonBody = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val response = khttp.post(url = "$url/api/users", data = jsonBody)
        assertEquals(HttpStatus.CREATED_201, response.statusCode)
    }

    fun `testar se os campos obrigatórios estão sendo retornados`() {
        val phones = listOf(PhoneDTO(ddd = "88", number = "88888888"), PhoneDTO(ddd = "99", number = "99999999"))
        val user = NewUserDTO(name = "Jaspion", email = "jaspion@email.com", password = "showtime123", phones = phones)
        val jsonBody = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val response = khttp.post(url = "$url/api/users", data = jsonBody)
        val userCreated = response.text.toJsonObject(UserDTO::class.java)
        assertNotNull(userCreated.id)
        assertNotNull(userCreated.created)
        assertNotNull(userCreated.modified)
        assertNotNull(userCreated.lastLogin)
        assertNotNull(userCreated.token)
    }

    fun `testar se está validando e-mail já existente`() {
        val user1 = NewUserDTO(name = "user1", email = "user@email.com", password = "showtime123")
        val jsonBodyUser1 = JavalinJackson.getObjectMapper().writeValueAsString(user1)
        khttp.post(url = "$url/api/users", data = jsonBodyUser1)

        val user2 = NewUserDTO(name = "user2", email = "user@email.com", password = "showtime123")
        val jsonBodyUser2 = JavalinJackson.getObjectMapper().writeValueAsString(user2)

        val response = khttp.post(url = "$url/api/users", data = jsonBodyUser2)
        assertEquals("E-mail já existente", response.jsonObject["mensagem"])
    }

    fun `testar validar campo nome não preenchido`() {
        val user = NewUserDTO(email = "ironuser@mail.com", password = "iron123")
        val jsonBody = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val response = khttp.post(url = "$url/api/users", data = jsonBody)
        val applicationErrorResponse = response.text.toJsonObject(ApplicationErrorResponse::class.java)
        assertEquals("Campo nome obrigatório", applicationErrorResponse.mensagem)
    }

    fun `testar validar campo email não preenchido`() {
        val user = NewUserDTO(name = "user", password = "iron123")
        val jsonBody = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val response = khttp.post(url = "$url/api/users", data = jsonBody)
        val applicationErrorResponse = response.text.toJsonObject(ApplicationErrorResponse::class.java)
        assertEquals("Campo email obrigatório", applicationErrorResponse.mensagem)
    }

    fun `testar validar campo password não preenchido`() {
        val user = NewUserDTO(name = "user", email = "user@mail.com")
        val jsonBody = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val response = khttp.post(url = "$url/api/users", data = jsonBody)
        val applicationErrorResponse = response.text.toJsonObject(ApplicationErrorResponse::class.java)
        assertEquals("Campo password obrigatório", applicationErrorResponse.mensagem)
    }

    fun `testar validar campo de email não preenchido no login`() {
        val loginDTO = LoginDTO(password = "123")
        val loginDTOJson = JavalinJackson.getObjectMapper().writeValueAsString(loginDTO)
        val response = khttp.post(url = "$url/api/login", data = loginDTOJson)
        val applicationErrorResponse = response.text.toJsonObject(ApplicationErrorResponse::class.java)
        assertEquals("Campo email deve ser preenchido", applicationErrorResponse.mensagem)
    }

    fun `testar validar campo de password não preenchido no login`() {
        val loginDTO = LoginDTO(email = "user@mail.com")
        val loginDTOJson = JavalinJackson.getObjectMapper().writeValueAsString(loginDTO)
        val response = khttp.post(url = "$url/api/login", data = loginDTOJson)
        val applicationErrorResponse = response.text.toJsonObject(ApplicationErrorResponse::class.java)
        assertEquals("Campo password deve ser preenchido", applicationErrorResponse.mensagem)
    }

    fun `testar se o usuário criado é o mesmo retornado pelo login`() {

        // cria um usuário
        val phones = listOf(PhoneDTO(ddd = "99", number = "99999999"))
        val user = NewUserDTO(name = "User", email = "user12345@email.com", password = "showtime123", phones = phones)
        val userJson = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val userCreatedResponse = khttp.post(url = "$url/api/users", data = userJson)
        val userCreated = userCreatedResponse.text.toJsonObject(UserDTO::class.java)

        val loginDTO = LoginDTO(email = user.email, password = user.password)

        val loginJson = JavalinJackson.getObjectMapper().writeValueAsString(loginDTO)
        val loginResponse = khttp.post(url = "$url/api/login", data = loginJson)
        val loginUser = loginResponse.text.toJsonObject(UserDTO::class.java)

        assertEquals(userCreated.id, loginUser.id)
        assertNotSame(userCreated.lastLogin, loginUser.lastLogin)

    }

    fun `testar mensagem de erro se houver uma tentativa de login com dados não existentes`() {

        val loginDTO = LoginDTO(email = "user123@email.com", password = "showtime123")

        val loginJson = JavalinJackson.getObjectMapper().writeValueAsString(loginDTO)
        val loginResponse = khttp.post(url = "$url/api/login", data = loginJson)
        val applicationErrorResponse = loginResponse.text.toJsonObject(ApplicationErrorResponse::class.java)

        assertEquals(HttpStatus.NOT_FOUND_404, loginResponse.statusCode)
        assertEquals("Usuário e/ou senha inválidos", applicationErrorResponse.mensagem)

    }

    fun `testar mensagem de validação do login quando a senha do usuário é diferente`() {

        val phones = listOf(PhoneDTO(ddd = "99", number = "99999999"))
        val user = NewUserDTO(name = "User", email = "user123@email.com", password = "showtime123", phones = phones)
        val userJson = JavalinJackson.getObjectMapper().writeValueAsString(user)
        khttp.post(url = "$url/api/users", data = userJson)

        val loginDTO = LoginDTO(email = "user123@email.com", password = "showtime1234")

        val loginJson = JavalinJackson.getObjectMapper().writeValueAsString(loginDTO)
        val loginResponse = khttp.post(url = "$url/api/login", data = loginJson)
        val applicationErrorResponse = loginResponse.text.toJsonObject(ApplicationErrorResponse::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED_401, loginResponse.statusCode)
        assertEquals("Usuário e/ou senha inválidos", applicationErrorResponse.mensagem)

    }

    fun `testar acesso não autorizado se a requisição não conter token de usuário`() {

        val phones = listOf(PhoneDTO(ddd = "99", number = "99999999"))
        val user = NewUserDTO(name = "User", email = "user123@email.com", password = "showtime123", phones = phones)
        val userJson = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val userCreatedResponse = khttp.post(url = "$url/api/users", data = userJson)
        val userCreated = userCreatedResponse.text.toJsonObject(UserDTO::class.java)

        val getResponse = khttp.get(url = "$url/api/users/${userCreated.id}")
        val applicationErrorResponse = getResponse.text.toJsonObject(ApplicationErrorResponse::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED_401, getResponse.statusCode)
        assertEquals("Não autorizado", applicationErrorResponse.mensagem)

    }


    fun `testar validação de erro quando o token passado na request é diferente do token armazenado em banco`() {

        val phones = listOf(PhoneDTO(ddd = "99", number = "99999999"))
        val user = NewUserDTO(name = "User", email = "user123@email.com", password = "showtime123", phones = phones)
        val userJson = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val userCreatedResponse = khttp.post(url = "$url/api/users", data = userJson)
        val userCreated = userCreatedResponse.text.toJsonObject(UserDTO::class.java)

        val getResponse =
            khttp.get(
                url = "$url/api/users/${userCreated.id}",
                auth = BearerTokenAuthorization("${userCreated.token.toString()}123")
            )
        val applicationErrorResponse = getResponse.text.toJsonObject(ApplicationErrorResponse::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED_401, getResponse.statusCode)
        assertEquals("Não autorizado", applicationErrorResponse.mensagem)

    }

    fun `testar validação de erro quando o ultimo login foi a mais de 30 minutos`() {

        val phones = listOf(PhoneDTO(ddd = "99", number = "99999999"))
        val user = NewUserDTO(name = "User", email = "user123@email.com", password = "showtime123", phones = phones)
        val userJson = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val userCreatedResponse = khttp.post(url = "$url/api/users", data = userJson)
        val userCreated = userCreatedResponse.text.toJsonObject(UserDTO::class.java)

        val loginDTO = LoginDTO(email = user.email, password = user.password)

        val loginJson = JavalinJackson.getObjectMapper().writeValueAsString(loginDTO)
        val loginResponse1 = khttp.post(url = "$url/api/login", data = loginJson)
        val loginUser1 = loginResponse1.text.toJsonObject(UserDTO::class.java)

        val futureDate = loginUser1.lastLogin?.plusMinutes(31)

        DateTimeUtils.setCurrentMillisFixed(futureDate!!.millis)

        val getResponse =
            khttp.get(
                url = "$url/api/users/${userCreated.id}",
                auth = BearerTokenAuthorization("${userCreated.token.toString()}")
            )

        val applicationErrorResponse = getResponse.text.toJsonObject(ApplicationErrorResponse::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED_401, getResponse.statusCode)
        assertEquals("Sessão inválida", applicationErrorResponse.mensagem)

    }

    fun `testar retorno do perfil de usuários`() {

        val phones = listOf(PhoneDTO(ddd = "99", number = "99999999"))
        val user = NewUserDTO(name = "User", email = "user123@email.com", password = "showtime123", phones = phones)
        val userJson = JavalinJackson.getObjectMapper().writeValueAsString(user)
        val userCreatedResponse = khttp.post(url = "$url/api/users", data = userJson)
        val userCreated = userCreatedResponse.text.toJsonObject(UserDTO::class.java)

        val loginDTO = LoginDTO(email = user.email, password = user.password)

        val loginJson = JavalinJackson.getObjectMapper().writeValueAsString(loginDTO)
        val loginResponse1 = khttp.post(url = "$url/api/login", data = loginJson)
        loginResponse1.text.toJsonObject(UserDTO::class.java)

        val getResponse =
            khttp.get(
                url = "$url/api/users/${userCreated.id}",
                auth = BearerTokenAuthorization("${userCreated.token.toString()}")
            )

        val resultUser = getResponse.text.toJsonObject(UserDTO::class.java)

        assertNotNull(resultUser.id)
        assertNotNull(resultUser.created)
        assertNotNull(resultUser.modified)
        assertNotNull(resultUser.lastLogin)
        assertNotNull(resultUser.token)

    }

}

