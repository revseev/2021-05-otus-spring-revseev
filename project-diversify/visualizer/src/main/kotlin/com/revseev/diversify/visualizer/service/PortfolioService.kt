package com.revseev.diversify.visualizer.service

import com.revseev.diversify.visualizer.controller.FilterConfig
import com.revseev.diversify.visualizer.domain.PortfolioByDiscriminator
import com.revseev.diversify.visualizer.domain.PortfolioItem
import com.revseev.diversify.visualizer.domain.PortfolioItemDto
import org.javamoney.moneta.FastMoney
import org.springframework.stereotype.Service
import javax.money.convert.ExchangeRateProvider
import kotlin.math.pow
import kotlin.math.roundToInt

@Service
class PortfolioService(
    private val coreClient: CoreClient,
    exchangeRateProvider: ExchangeRateProvider
) {

    private val rubConversion = exchangeRateProvider.getCurrencyConversion(RUB)

    fun getPortfolioWithFilters(
        userId: Int,
        filterConfig: FilterConfig,
    ): List<PortfolioItemDto> {
        val portfolio = coreClient.getPortfolioByUserId(userId)
        val (items, percentageMultiplier) = filterByAndCalculateTotalPercentageMultiplier(portfolio.items, filterConfig)

        return items.map {
            it.toDto().apply {
                percentage = it.totalPriceRub!!.asPercentage(percentageMultiplier)
            }
        }
    }

    fun getPortfolioWithFilters(
        userId: Int,
        filterConfig: FilterConfig,
        discriminator: (PortfolioItem) -> Any?
    ): List<PortfolioByDiscriminator> {
        val portfolio = coreClient.getPortfolioByUserId(userId)
        val (items, percentageMultiplier) = filterByAndCalculateTotalPercentageMultiplier(portfolio.items, filterConfig)

        return items
            .groupBy(discriminator)
            .asSequence()
            .map { (discriminator, items) ->
                val totalInCategory = items.fold(FastMoney.of(0, RUB)) { acc, item ->
                    acc.add(item.totalPriceRub)
                }
                PortfolioByDiscriminator(
                    discriminator = discriminator,
                    totalPriceRub = totalInCategory.toString(),
                    percentage = totalInCategory.asPercentage(percentageMultiplier),
                    items = items.sortedByDescending { it.totalPriceRub }
                        .map { it.toDto() }
                )
            }
            .sortedByDescending { it.percentage }
            .toList()
    }

    internal fun FastMoney.asPercentage(
        percentageMultiplier: Double
    ) = (number.doubleValueExact() * percentageMultiplier).roundTo(2)

    private fun filterByAndCalculateTotalPercentageMultiplier(
        portfolioItems: Collection<PortfolioItem>,
        filterConfig: FilterConfig
    ): Pair<List<PortfolioItem>, Double> {
        var sum = 0.0
        val items = portfolioItems.asSequence()
            .filter { filterConfig.accountFilter?.contains(it.accountId) ?: true }
            .filter { filterConfig.assetTypeFilter?.contains(it.assetType) ?: true }
            .filter { filterConfig.sectorFilter?.contains(it.sector) ?: true }
            .filter { filterConfig.countryOfRiskFilter?.contains(it.countryOfRiskCode) ?: true }
            .onEach {
                val rubPrice = it.totalPrice.with(rubConversion)
                it.totalPriceRub = rubPrice
                sum += rubPrice.number.doubleValueExact()
            }
            .sortedByDescending { it.totalPriceRub }
            .toList()

        val percentageMultiplier = if (sum != 0.0) {
            100 / sum
        } else {
            sum
        }
        return Pair(items, percentageMultiplier)
    }

    private fun PortfolioItem.toDto(): PortfolioItemDto = PortfolioItemDto(
        accountId = this.accountId,
        assetType = this.assetType,
        figi = this.figi,
        name = this.name,
        countryOfRiskCode = this.countryOfRiskCode,
        sector = this.sector,
        quantity = this.quantity.stripTrailingZeros().toPlainString(),
        unitPrice = this.unitPrice.toString(),
        totalPrice = this.totalPrice.toString(),
        totalPriceRub = (this.totalPriceRub ?: this.totalPrice.with(rubConversion)).toString()
    )

    companion object {
        private const val RUB = "RUB"
    }
}

fun Double.roundTo(numFractionDigits: Int): Double {
    val factor = 10.0.pow(numFractionDigits.toDouble())
    return (this * factor).roundToInt() / factor
}