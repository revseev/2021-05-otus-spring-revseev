package com.revseev.diversify.visualizer.controller

import com.revseev.diversify.visualizer.domain.AssetType
import com.revseev.diversify.visualizer.domain.PortfolioByDiscriminator
import com.revseev.diversify.visualizer.domain.PortfolioItemDto
import com.revseev.diversify.visualizer.service.PortfolioService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.*
import java.util.*

private val log = KotlinLogging.logger { }

@RestController
@RequestMapping("api/v1")
class PortfolioController(
    private val portfolioService: PortfolioService
) {

    @PostMapping("users/{userId}/portfolio")
    fun getPortfolioByUserId(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioItemDto> {
        val items = portfolioService.getPortfolioWithFilters(userId, filterConfig)
        return items
    }

    @PostMapping("users/{userId}/portfolio/by-asset-type")
    fun getPortfolioByUserIdGroupedByAssetType(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioByDiscriminator> {
        val items = portfolioService.getPortfolioWithFilters(userId, filterConfig) { it.assetType }
        return items
    }

    @PostMapping("users/{userId}/portfolio/by-country")
    fun getPortfolioByUserIdGroupedByCountry(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioByDiscriminator> {
        val items = portfolioService.getPortfolioWithFilters(userId, filterConfig) { it.countryOfRiskCode }
        return items
    }

    @PostMapping("users/{userId}/portfolio/by-sector")
    fun getPortfolioByUserIdGroupedBySector(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioByDiscriminator> {
        val items = portfolioService.getPortfolioWithFilters(userId, filterConfig) { it.sector }
        return items
    }
}

data class FilterConfig(
    val accountFilter: Set<String>? = null,
    val assetTypeFilter: EnumSet<AssetType>? = null,
    val countryOfRiskFilter: Set<String>? = null,
    val sectorFilter: Set<String>? = null,
)