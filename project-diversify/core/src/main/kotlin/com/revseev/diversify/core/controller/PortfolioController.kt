package com.revseev.diversify.core.controller

import com.revseev.diversify.core.domain.Portfolio
import com.revseev.diversify.core.service.PortfolioService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger { }

@RestController
class PortfolioController(
    private val portfolioService: PortfolioService
) {

    @GetMapping("users/{userId}/portfolio")
    fun getUserPortfolio(@PathVariable userId: Int): Portfolio {
        log.info { "[GET: users/$userId/portfolio]" }
        return portfolioService.getPortfolioByUserId(userId)
    }
}