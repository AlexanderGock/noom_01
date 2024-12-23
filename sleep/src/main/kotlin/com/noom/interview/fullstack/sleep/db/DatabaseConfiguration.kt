package com.noom.interview.fullstack.sleep.db

import com.noom.interview.fullstack.sleep.SleepApplication.Companion.UNIT_TEST_PROFILE
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.sql.Connection
import javax.sql.DataSource


@Configuration
@Profile("!$UNIT_TEST_PROFILE")
@EnableJpaRepositories(basePackages = ["com.noom.interview.fullstack.sleep.db.repository"])
@EntityScan(basePackages = ["com.noom.interview.fullstack.sleep.db.entity"])
@EnableTransactionManagement
class DatabaseConfiguration {
    @Value("\${spring.datasource.url}")
    private val url: String? = null

    @Value("\${spring.datasource.username}")
    private val username: String? = null

    @Value("\${spring.datasource.password}")
    private val password: String? = null

    @Bean
    fun dataSource(): DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName("org.postgresql.Driver")
        dataSourceBuilder.url(url)
        dataSourceBuilder.username(username)
        dataSourceBuilder.password(password)
        return dataSourceBuilder.build()
    }

    @Bean
    fun dbConnection(dataSource: DataSource) : Connection
        = dataSource.connection

    @Bean
    fun namedParameterJdbcTemplate(dataSource: DataSource) : NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }
}