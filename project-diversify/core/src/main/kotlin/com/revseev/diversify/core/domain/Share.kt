package com.revseev.diversify.core.domain

/**Объект передачи информации об акции.*/
data class Share( //todo remove?
	val figi: String? = null,
	val tradingStatus: String? = null,
	val shareType: String? = null,
	val countryOfRisk: String? = null,
	val lot: Int? = null,
	val uid: String? = null,
	val nominal: Nominal? = null,
	val sellAvailableFlag: Boolean? = null,
	val currency: String? = null,
	val sector: String? = null,
	val realExchange: String? = null,
	val buyAvailableFlag: Boolean? = null,
	val classCode: String? = null,
	val ticker: String? = null,
	val apiTradeAvailableFlag: Boolean? = null,
	val shortEnabledFlag: Boolean? = null,
	val issueSizePlan: String? = null,
	val minPriceIncrement: MinPriceIncrement? = null,
	val otcFlag: Boolean? = null,
	val name: String? = null,
	val issueSize: String? = null,
	val exchange: String? = null,
	val countryOfRiskName: String? = null,
	val divYieldFlag: Boolean? = null,
	val isin: String? = null,
	val ipoDate: String? = null,
)

data class Nominal(
	val nano: Int? = null,
	val currency: String? = null,
	val units: String? = null,
)

data class MinPriceIncrement(
	val nano: Int? = null,
	val units: String? = null,
)

