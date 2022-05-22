package com.revseev.diversify.visualizer.controller

import com.revseev.diversify.visualizer.domain.FilterConfig
import com.revseev.diversify.visualizer.domain.PortfolioByDiscriminator
import com.revseev.diversify.visualizer.domain.PortfolioItemDto
import com.revseev.diversify.visualizer.service.PortfolioService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.*

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
        log.info { "[POST: users/$userId/portfolio]" }
        return portfolioService.getPortfolioWithFilters(userId, filterConfig)
    }

    @PostMapping("users/{userId}/portfolio/by-asset-type")
    fun getPortfolioByUserIdGroupedByAssetType(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioByDiscriminator> {
        log.info { "[POST: users/$userId/portfolio/by-asset-type]" }
        return portfolioService.getPortfolioWithFilters(userId, filterConfig) { it.assetType }
    }

    @PostMapping("users/{userId}/portfolio/by-country")
    fun getPortfolioByUserIdGroupedByCountry(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioByDiscriminator> {
        log.info { "[POST: users/$userId/portfolio/by-country]" }
        return portfolioService.getPortfolioWithFilters(userId, filterConfig) { it.countryOfRiskCode }
    }

    @PostMapping("users/{userId}/portfolio/by-sector")
    fun getPortfolioByUserIdGroupedBySector(
        @PathVariable userId: Int,
        @RequestBody filterConfig: FilterConfig
    ): List<PortfolioByDiscriminator> {
        log.info { "[POST: users/$userId/portfolio/by-sector]" }
        return portfolioService.getPortfolioWithFilters(userId, filterConfig) { it.sector }
    }
}