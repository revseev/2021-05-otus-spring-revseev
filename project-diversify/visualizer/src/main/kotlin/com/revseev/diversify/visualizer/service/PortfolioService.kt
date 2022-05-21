package com.revseev.diversify.visualizer.service

import com.revseev.diversify.visualizer.controller.VisualConfig
import com.revseev.diversify.visualizer.domain.PortfolioItem
import org.springframework.stereotype.Service

@Service
class PortfolioService(
    private val coreClient: CoreClient
) {


    fun getPortfolioWithFilters(userId: Int, visualConfig: VisualConfig): List<PortfolioItem> {
        val portfolio = coreClient.getPortfolioByUserId(userId)

        return portfolio.items.asSequence()
            .filter { visualConfig.accountFilter?.contains(it.accountId) ?: true }
            .filter { visualConfig.assetTypeFilter?.contains(it.assetType) ?: true }
            .filter { visualConfig.sectorFilter?.contains(it.sector) ?: true }
            .filter { visualConfig.countryOfRiskFilter?.contains(it.countryOfRiskCode) ?: true }
            .sortedByDescending {
                it.totalPrice
            }
            .toList()
    }
}