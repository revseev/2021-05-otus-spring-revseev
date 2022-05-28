package com.revseev.diversify.core.controller

import com.revseev.diversify.core.domain.AssetCustomData
import com.revseev.diversify.core.service.AssetCustomDataService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger { }

@RestController
class AssetCustomDataController(
    private val acdService: AssetCustomDataService
) {

    @PostMapping("users/{userId}/asset/{figi}/custom")
    fun saveAssetCustomData(
        @PathVariable userId: Int,
        @PathVariable figi: String,
        @RequestBody data: AssetCustomData
    ) {
        log.info { "[POST: users/$userId/asset/$figi/custom]" }
        acdService.saveCustomData(userId, figi, data)
    }
}