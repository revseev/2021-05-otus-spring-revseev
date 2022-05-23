package com.revseev.diversify.visualizer.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.revseev.diversify.visualizer.service.mapper.PortfolioDeserializer
import org.javamoney.moneta.FastMoney
import java.math.BigDecimal


@JsonDeserialize(using = PortfolioDeserializer::class)
data class Portfolio(
    val accounts: List<Account>,
    val items: List<PortfolioItem>,
)

data class PortfolioItem(
    val accountId: String,
    val assetType: AssetType,
    val figi: String,
    val name: String,
    var countryOfRiskCode: String,
    var countryOfRisk: String,
    var sector: String,
    val quantity: BigDecimal,
    val unitPrice: FastMoney,
    val totalPrice: FastMoney
) {
    var totalPriceRub: FastMoney? = null
}

data class PortfolioItemDto(
    val accountId: String,
    val assetType: AssetType,
    val figi: String,
    val name: String,
    val countryOfRiskCode: String,
    val countryOfRisk: String,
    val sector: String,
    val quantity: String,
    val unitPrice: String,
    val totalPrice: String,
    val totalPriceRub: String,
) {
    var percentage: Double? = null
}

data class PortfolioByDiscriminator(
    val discriminator: Any?,
    val totalPriceRub: String,
    val percentage: Double,
    val items: List<PortfolioItemDto>
)