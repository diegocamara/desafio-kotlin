package com.application.config.koin

import com.application.config.route.RouteConfig
import com.application.controller.LoginController
import com.application.controller.UserController
import com.application.dao.PhoneDAO
import com.application.dao.impl.PhoneDAOImpl
import com.application.dao.UserDAO
import com.application.dao.impl.UserDAOImpl
import com.application.service.LoginService
import com.application.service.PhoneService
import com.application.service.impl.PhoneServiceImpl
import com.application.service.UserService
import com.application.service.impl.LoginServiceImpl
import com.application.service.impl.UserServiceImpl
import org.koin.dsl.module.module
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

object KoinModuleConfig {
    val applicationModule = module {
        single<PhoneDAO> { PhoneDAOImpl() }
        single<PhoneService> { PhoneServiceImpl(get()) }
        single<UserDAO> { UserDAOImpl() }
        single<UserService> { UserServiceImpl(get(), get(), get()) }
        single<UserController> { UserController(get()) }
        single<LoginService> { LoginServiceImpl(get(), get()) }
        single<LoginController> { LoginController(get()) }
        single<PasswordEncoder> { BCryptPasswordEncoder() }
        single<RouteConfig> { RouteConfig(get(), get()) }
    }
}