package com.revseev.diversify.core.domain


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
    val quantity: String,
    val currency: String,
    val unitPrice: String,
)