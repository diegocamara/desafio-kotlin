package com.application.config.persistence

import com.application.domain.Phones
import com.application.domain.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    private val schemas: Array<Table> = arrayOf(Users, Phones)

    fun init(createSchema: Boolean = false) {
        Database.connect(hikari())
        if (createSchema) {
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(*schemas)
            }
        }
    }

    fun drop() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.drop(*schemas)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:test"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)

    }

}