package com.revseev.diversify.visualizer.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.money.convert.ExchangeRateProvider
import javax.money.convert.MonetaryConversions

@Configuration
class CurrencyConversionConfig {

    @Bean
    fun getExchangeRateProvider(): ExchangeRateProvider = MonetaryConversions.getExchangeRateProvider("IMF")
}