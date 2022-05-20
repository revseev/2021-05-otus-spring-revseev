package com.revseev.diversify.visualizer.domain

import org.javamoney.moneta.FastMoney
import java.math.BigDecimal


data class Portfolio(
    val accounts: List<Account>,
    val items: List<PortfolioItem>,
)

data class PortfolioItem(
    val accountId: String,
    val assetType: AssetType,
    val figi: String,
    val name: String,
    val countryOfRiskCode: String,
    val sector: String,
    val quantity: BigDecimal,
    val unitPrice: FastMoney,
    val totalPrice: FastMoney
)