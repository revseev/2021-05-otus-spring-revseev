package com.revseev.diversify.visualizer.controller

import com.revseev.diversify.visualizer.domain.Account
import com.revseev.diversify.visualizer.service.AccountService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.*

private val log = KotlinLogging.logger { }

@RestController
@RequestMapping("api/v1")
class AccountsController(
    private val accountService: AccountService
) {

    @GetMapping("users/{userId}/accounts")
    fun getPortfolioByUserId(@PathVariable userId: Int): List<Account> {
        log.info { "[GET: users/$userId/accounts]" }
        return accountService.getAccountsByUserId(userId)
    }
}