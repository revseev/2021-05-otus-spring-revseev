package com.revseev.diversify.visualizer.service

import com.revseev.diversify.visualizer.domain.FilterConfig
import com.revseev.diversify.visualizer.domain.PortfolioByDiscriminator
import com.revseev.diversify.visualizer.domain.PortfolioItem
import com.revseev.diversify.visualizer.domain.PortfolioItemDto
import org.javamoney.moneta.FastMoney
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.math.roundToInt


@Service
class VisualizerService(
    private val portfolioService: PortfolioService,
    private val convertor: CurrencyConvertor
) {

    fun getPortfolioWithFilters(
        userId: Int,
        filterConfig: FilterConfig,
    ): List<PortfolioItemDto> {
        val portfolioItems = portfolioService.getPortfolioItemsByUserId(userId)
        val (items, percentageMultiplier) = filterByAndCalculateTotalPercentageMultiplier(portfolioItems, filterConfig)

        return items.map {
            it.toDto().apply {
                percentage = it.totalPriceRub!!.asPercentage(percentageMultiplier)
            }
        }
    }

    fun getPortfolioWithFiltersGroupedBy(
        userId: Int,
        filterConfig: FilterConfig,
        discriminator: (PortfolioItem) -> Any?
    ): List<PortfolioByDiscriminator> {
        val portfolioItems = portfolioService.getPortfolioItemsByUserId(userId)
        val (items, percentageMultiplier) = filterByAndCalculateTotalPercentageMultiplier(portfolioItems, filterConfig)

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

    private fun filterByAndCalculateTotalPercentageMultiplier(
        portfolioItems: Sequence<PortfolioItem>,
        filterConfig: FilterConfig
    ): Pair<List<PortfolioItem>, Double> {
        var sum = 0.0
        val items = portfolioItems.asSequence()
            .filter { filterConfig.accountFilter?.contains(it.accountId) ?: true }
            .filter { filterConfig.assetTypeFilter?.contains(it.assetType) ?: true }
            .filter { filterConfig.sectorFilter?.contains(it.sector) ?: true }
            .filter { filterConfig.countryOfRiskFilter?.contains(it.countryOfRiskCode) ?: true }
            .onEach {
                val rubPrice = convertor.toRUB(it.totalPrice)
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
        countryOfRisk = this.countryOfRisk,
        sector = this.sector,
        quantity = this.quantity.stripTrailingZeros().toPlainString(),
        unitPrice = this.unitPrice.toString(),
        totalPrice = this.totalPrice.toString(),
        totalPriceRub = (this.totalPriceRub ?: convertor.toRUB(this.totalPrice)).toString()
    )

    internal fun FastMoney.asPercentage(
        percentageMultiplier: Double
    ) = (number.doubleValueExact() * percentageMultiplier).roundTo(2)

    companion object {
        private const val RUB = "RUB"
    }
}

fun Double.roundTo(numFractionDigits: Int): Double {
    val factor = 10.0.pow(numFractionDigits.toDouble())
    return (this * factor).roundToInt() / factor
}