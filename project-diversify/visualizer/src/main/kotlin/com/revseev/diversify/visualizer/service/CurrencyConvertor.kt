@file:Suppress("UNCHECKED_CAST")

package com.revseev.diversify.visualizer.service

import org.springframework.stereotype.Component
import javax.money.MonetaryAmount
import javax.money.convert.MonetaryConversions

@Component
class CurrencyConvertor {

    private val rateProvider = MonetaryConversions.getExchangeRateProvider("IMF")
    private val rubConversion = rateProvider.getCurrencyConversion("RUB")

    fun <T : MonetaryAmount> toRUB(monetaryAmount: T): T {
        val monetaryAmount1: MonetaryAmount = monetaryAmount
        val amount: MonetaryAmount = monetaryAmount1.with(rubConversion)
        return amount as T
    }
}