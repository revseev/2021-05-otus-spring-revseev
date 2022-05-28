package com.revseev.diversify.core.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.tinkoff.piapi.core.InvestApi

@Configuration
class TinkoffApiConfig(
    @Value("\${tinkoff.access-token}")
    private val token: String
) {

    @Bean
    fun tinkoffApi(): InvestApi = InvestApi.create(token)
}