package com.revseev.diversify.visualizer.service.mapper

import com.fasterxml.jackson.module.kotlin.readValue
import com.revseev.diversify.visualizer.config.SerializationConfig
import com.revseev.diversify.visualizer.domain.Portfolio
import org.junit.jupiter.api.Test
import strikt.api.expectCatching
import strikt.assertions.isSuccess

internal class PortfolioDeserializerTest {

    private val mapper = SerializationConfig().objectMapper()

    @Test
    fun `should deserialize Portfolio without errors`() {
        expectCatching {
            val portfolio = mapper.readValue<Portfolio>(portfolioJson)
            println(portfolio)
        }.isSuccess()
    }
}

private const val portfolioJson = """{
  "accounts": [
    {
      "providerId": "100000000",
      "name": "Брокерский счёт",
      "type": "TINKOFF",
      "userId": 1
    },
    {
      "providerId": "100000001",
      "name": "ИИС",
      "type": "TINKOFF_IIS",
      "userId": 1
    }
  ],
  "items": [
    {
      "accountId": "100000000",
      "assetType": "SHARE",
      "figi": "BBG000DHPN63",
      "name": "Realty Income REIT",
      "countryOfRiskCode": "US",
      "sector": "real_estate",
      "quantity": "2.000000000",
      "currency": "USD",
      "unitPrice": "66.990000000"
    },
    {
      "accountId": "100000001",
      "assetType": "BOND",
      "figi": "BBG00R4C0L75",
      "name": "ПИК-Корпорация выпуск 2",
      "countryOfRiskCode": "RU",
      "sector": "real_estate",
      "quantity": "5.000000000",
      "currency": "RUB",
      "unitPrice": "886.800000000"
    },
    {
      "accountId": "100000001",
      "assetType": "ETF",
      "figi": "BBG000000002",
      "name": "Тинькофф Вечный портфель EUR",
      "countryOfRiskCode": "EU",
      "sector": "-",
      "quantity": "200.000000000",
      "currency": "EUR",
      "unitPrice": "0.101700000"
    }
  ]
}"""