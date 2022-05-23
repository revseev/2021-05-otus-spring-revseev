package com.revseev.diversify.visualizer.mapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.revseev.diversify.visualizer.domain.Account
import com.revseev.diversify.visualizer.domain.Portfolio
import com.revseev.diversify.visualizer.domain.PortfolioItem
import org.javamoney.moneta.FastMoney
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode

class PortfolioDeserializer : JsonDeserializer<Portfolio>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): Portfolio {
        val node: JsonNode = jp.codec.readTree(jp)
        return Portfolio(
            accounts = buildList {
                for (accountNode in node.get("accounts").elements()) {
                    add(
                        Account(
                            id = accountNode.get("id").asText(),
                            name = accountNode.get("name").asText(),
                            type = enumValueOf(accountNode.get("type").asText()),
                            userId = accountNode.get("userId").asInt()
                        )
                    )
                }
            },
            items = buildList {
                for (itemNode in node.get("items").elements()) {
                    val quantity = BigDecimal(itemNode.get("quantity").asText()).setScale(5, RoundingMode.HALF_EVEN)
                    val unitPrice = FastMoney.of(
                        BigDecimal(itemNode.get("unitPrice").asText()).setScale(5, RoundingMode.HALF_EVEN),
                        itemNode.get("currency").asText()
                    )
                    add(
                        PortfolioItem(
                            accountId = itemNode.get("accountId").asText(),
                            assetType = enumValueOf(itemNode.get("assetType").asText()),
                            figi = itemNode.get("figi").asText(),
                            name = itemNode.get("name").asText(),
                            countryOfRiskCode = itemNode.get("countryOfRiskCode").asText(),
                            countryOfRisk = itemNode.get("countryOfRisk").asText(),
                            sector = itemNode.get("sector").asText(),
                            quantity = quantity,
                            unitPrice = unitPrice,
                            totalPrice = unitPrice.multiply(quantity),
                        )
                    )
                }
            }
        )
    }
}