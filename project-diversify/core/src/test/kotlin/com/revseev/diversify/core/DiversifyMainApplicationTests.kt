package com.revseev.diversify.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.protobuf.util.JsonFormat
import com.revseev.diversify.core.dao.AssetDao
import com.revseev.diversify.core.service.PortfolioService
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.tinkoff.piapi.core.InvestApi
import ru.tinkoff.piapi.core.models.Portfolio
import ru.tinkoff.piapi.core.models.Positions

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiversifyMainApplicationTests {

    @Autowired
    lateinit var assetDao: AssetDao

    @Autowired
    lateinit var tinkoffApi: InvestApi

    @Autowired
    lateinit var printer: JsonFormat.Printer

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var portfolioService: PortfolioService

    @Test
    fun contextLoads() {
    }

    @Test
    @Disabled
    fun `fetch all assets and load them into db`() {
        val instrumentsService = tinkoffApi.instrumentsService
        val shares = instrumentsService.allSharesSync
        assetDao.loadShares(shares)
        val etfs = instrumentsService.allEtfsSync
        assetDao.loadEtfs(etfs)
        val bonds = instrumentsService.allBondsSync
        assetDao.loadBonds(bonds)
        val currencies = instrumentsService.allCurrenciesSync
        assetDao.loadCurrencies(currencies)
    }

    @Test
    fun `should load portfolio data`() {
        val operationsService = tinkoffApi.operationsService
        val accounts = tinkoffApi.userService.accountsSync
        accounts.forEach {
            val accountId = it.id
            val portfolio: Portfolio = operationsService.getPortfolioSync(accountId)
            val positions: Positions = operationsService.getPositionsSync(accountId)
            println("--------------------------------")
            println(mapper.writeValueAsString(portfolio))
            println("--------------------------------")
            println(mapper.writeValueAsString(positions))

            portfolioService.savePortfolio(it, portfolio, positions)
        }
    }
}
