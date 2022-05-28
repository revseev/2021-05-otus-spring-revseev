package com.revseev.diversify.visualizer.service

import com.revseev.diversify.visualizer.domain.PortfolioItem
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger { }

@Service
class PortfolioService(
    private val coreClient: CoreClient,
    private val customDataStorage: AssetCustomDataStorage,
) {

    fun getPortfolioItemsByUserId(userId: Int): Sequence<PortfolioItem> {
        log.info { "Loading portfolio for userId = $userId" }
        val assetCustomDataMap = customDataStorage.forUser(userId)
        return coreClient.getPortfolioByUserId(userId).items.asSequence()
            .onEach { item ->
                assetCustomDataMap[item.figi]?.let { customData ->
                    log.info { "Found custom data in ACDStorage for FIGI = ${item.figi}, $customData" }
                    customData.countryOfRiskName?.let { item.countryOfRisk = it }
                    customData.countryOfRisk?.let { item.countryOfRiskCode = it }
                    customData.sector?.let { item.sector = it }
                }
            }
    }
}