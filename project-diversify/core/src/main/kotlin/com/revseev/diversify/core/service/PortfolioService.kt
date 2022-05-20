package com.revseev.diversify.core.service

import com.revseev.diversify.core.dao.AccountDao
import com.revseev.diversify.core.dao.PortfolioDao
import com.revseev.diversify.core.domain.Account
import com.revseev.diversify.core.domain.Portfolio
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tinkoff.piapi.core.models.Positions
import ru.tinkoff.piapi.contract.v1.Account as TinkoffAccount
import ru.tinkoff.piapi.core.models.Portfolio as TinkoffPortfolio

private val log = KotlinLogging.logger { }


@Service
class PortfolioService(
    private val accountDao: AccountDao,
    private val portfolioDao: PortfolioDao
) {

    @Transactional
    fun savePortfolio(account: TinkoffAccount, portfolio: TinkoffPortfolio, positions: Positions, userId: Int = 1) {
        log.info { "Saving portfolio information for user = $userId" }
        accountDao.saveAccount(
            userId = userId,
            providerId = account.id,
            type = Account.Type.from(account.type),
            name = account.name
        )
        portfolioDao.savePortfolio(userId, account.id, portfolio, positions)
    }

    @Transactional(readOnly = true)
    fun getPortfolioByUserId(userId: Int): Portfolio {
        log.info { "Finding account information for user = $userId" }
        val accounts: List<Account> = accountDao.getAccountsByUserId(userId)
        log.info { "Finding portfolio items for user = $userId" }
        val portfolioItems = portfolioDao.getPortfolioByUserId(userId)
        return Portfolio(accounts, portfolioItems)
    }
}