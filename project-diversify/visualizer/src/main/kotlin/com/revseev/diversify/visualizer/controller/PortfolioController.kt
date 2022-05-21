package com.revseev.diversify.visualizer.controller

import com.revseev.diversify.visualizer.domain.AssetType
import com.revseev.diversify.visualizer.domain.PortfolioItem
import com.revseev.diversify.visualizer.service.PortfolioService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/v1")
class PortfolioController(
    private val portfolioService: PortfolioService
) {

    @PostMapping("users/{userId}/portfolio")
    fun getPortfolioByUserId(@PathVariable userId: Int, @RequestBody visualConfig: VisualConfig): List<PortfolioItem> {
        val items = portfolioService.getPortfolioWithFilters(userId, visualConfig)
        return items
    }
}

data class VisualConfig(
    val accountFilter: Set<String>? = null,
    val assetTypeFilter: EnumSet<AssetType>? = null,
    val countryOfRiskFilter: Set<String>? = null,
    val sectorFilter: Set<String>? = null,
)