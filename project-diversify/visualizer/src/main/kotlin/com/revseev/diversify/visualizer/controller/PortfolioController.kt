package com.revseev.diversify.visualizer.controller

import com.revseev.diversify.visualizer.domain.FilterConfig
import com.revseev.diversify.visualizer.domain.PortfolioByDiscriminator
import com.revseev.diversify.visualizer.domain.PortfolioItemDto
import com.revseev.diversify.visualizer.service.CoreClient
import com.revseev.diversify.visualizer.service.VisualizerService
import mu.KotlinLogging
import org.springframework.cache.annotation.CacheEvict
import org.springframework.web.bind.annotation.*

private val log = KotlinLogging.logger { }

@RestController
@RequestMapping("api/v1")
class PortfolioController(
    private val visualizerService: VisualizerService,
    private val coreClient: CoreClient
) {

    @PostMapping("users/{userId}/portfolio")
    fun getPortfolioByUserId(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioItemDto> {
        log.info { "[POST: users/$userId/portfolio]" }
        return visualizerService.getPortfolioWithFilters(userId, filterConfig)
    }

    @PostMapping("users/{userId}/portfolio/by-asset-type")
    fun getPortfolioByUserIdGroupedByAssetType(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioByDiscriminator> {
        log.info { "[POST: users/$userId/portfolio/by-asset-type]" }
        return visualizerService.getPortfolioWithFiltersGroupedBy(userId, filterConfig) { it.assetType }
    }

    @PostMapping("users/{userId}/portfolio/by-country")
    fun getPortfolioByUserIdGroupedByCountry(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioByDiscriminator> {
        log.info { "[POST: users/$userId/portfolio/by-country]" }
        return visualizerService.getPortfolioWithFiltersGroupedBy(userId, filterConfig) { it.countryOfRiskCode }
    }

    @PostMapping("users/{userId}/portfolio/by-sector")
    fun getPortfolioByUserIdGroupedBySector(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioByDiscriminator> {
        log.info { "[POST: users/$userId/portfolio/by-sector]" }
        return visualizerService.getPortfolioWithFiltersGroupedBy(userId, filterConfig) { it.sector }
    }

    @PostMapping("users/{userId}/portfolio/update")
    @CacheEvict(value = ["portfolios"], key = "#root.args[0]")
    fun addLatestPortfolio(@PathVariable userId: Int) {
        log.info { "[POST: users/$userId/portfolio/update]" }
        coreClient.addLatestPortfolio(userId)
    }
}