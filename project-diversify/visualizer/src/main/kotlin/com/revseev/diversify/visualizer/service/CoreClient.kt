package com.revseev.diversify.visualizer.service

import com.revseev.diversify.visualizer.domain.AssetCustomData
import com.revseev.diversify.visualizer.domain.Portfolio
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient(name = "diversify-core")
interface CoreClient {

    @Cacheable("portfolios")
    @GetMapping("users/{userId}/portfolio")
    fun getPortfolioByUserId(@PathVariable userId: Int): Portfolio

    @PostMapping("users/{userId}/asset/{figi}/custom")
    fun saveAssetCustomData(
        @PathVariable userId: Int,
        @PathVariable figi: String,
        @RequestBody data: AssetCustomData
    )
}