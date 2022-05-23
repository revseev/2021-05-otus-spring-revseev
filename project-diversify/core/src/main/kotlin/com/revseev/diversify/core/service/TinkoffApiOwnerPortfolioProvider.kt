package com.revseev.diversify.core.service

import org.springframework.stereotype.Service
import ru.tinkoff.piapi.contract.v1.Account
import ru.tinkoff.piapi.core.InvestApi
import ru.tinkoff.piapi.core.models.Portfolio

@Service
class TinkoffApiOwnerPortfolioProvider(
    private val tinkoffApi: InvestApi
) {

    fun getLatestAccountsAndPortfolio(): List<Pair<Account, Portfolio>> {
        val operationsService = tinkoffApi.operationsService
        val accounts = tinkoffApi.userService.accountsSync

        return accounts.map { account ->
            val accountId = account.id
            val portfolio: Portfolio = operationsService.getPortfolioSync(accountId)
            account to portfolio
        }
    }
}