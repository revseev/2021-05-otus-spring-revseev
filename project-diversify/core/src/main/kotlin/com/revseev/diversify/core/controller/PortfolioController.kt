package com.revseev.diversify.core.controller

import com.revseev.diversify.core.domain.Portfolio
import com.revseev.diversify.core.service.PortfolioService
import com.revseev.diversify.core.service.TinkoffApiOwnerPortfolioProvider
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

private val log = KotlinLogging.logger { }

@RestController
class PortfolioController(
    private val portfolioService: PortfolioService,
    private val tinkoffPortfolioProvider: TinkoffApiOwnerPortfolioProvider
) {

    @GetMapping("users/{userId}/portfolio")
    fun getUserPortfolio(@PathVariable userId: Int): Portfolio {
        log.info { "[GET: users/$userId/portfolio]" }
        return portfolioService.getPortfolioByUserId(userId)
    }

    @PostMapping("users/{userId}/portfolio")
    fun addLatestPortfolio(@PathVariable userId: Int) {
        log.info { "[POST: users/$userId/portfolio] Saving latest portfolio data" }
        require(userId == 1) {
            "Only single user with id = 1 is supported at the moment"
        }
        tinkoffPortfolioProvider.getLatestAccountsAndPortfolio().forEach { (account, portfolio) ->
            portfolioService.savePortfolio(userId, account, portfolio)
        }
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(ex: IllegalArgumentException) {
        log.warn(ex) { "Handling IllegalArgumentException, setting 400 response status" }
    }
}