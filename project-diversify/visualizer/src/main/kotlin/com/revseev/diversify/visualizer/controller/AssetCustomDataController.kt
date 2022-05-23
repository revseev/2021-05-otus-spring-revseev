package com.revseev.diversify.visualizer.controller

import com.revseev.diversify.visualizer.domain.AssetCustomData
import com.revseev.diversify.visualizer.service.AssetCustomDataStorage
import com.revseev.diversify.visualizer.service.CoreClient
import mu.KotlinLogging
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

private val log = KotlinLogging.logger { }

@RestController
@RequestMapping("api/v1")
class AssetCustomDataController(
    private val acdStorage: AssetCustomDataStorage,
    private val coreClient: CoreClient
) {

    @PostMapping("users/{userId}/asset/{figi}/custom")
    fun saveAssetCustomData(
        @PathVariable userId: Int,
        @PathVariable figi: String,
        @RequestBody data: AssetCustomData
    ) {
        log.info { "[POST: users/$userId/asset/$figi/custom]" }
        acdStorage.forUser(userId)[figi] = data
        log.info { "Saved custom data for $userId and $figi into Storage: $data" }
        CompletableFuture.runAsync {
            coreClient.saveAssetCustomData(userId, figi, data)
            log.info { "Saved custom data for $userId and $figi into DB: $data" }
        }
    }
}