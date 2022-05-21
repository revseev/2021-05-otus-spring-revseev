package com.revseev.diversify.visualizer.service

import com.revseev.diversify.visualizer.domain.Portfolio
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient(name = "diversify-core")
interface CoreClient {

    @GetMapping("users/{userId}/portfolio")
    fun getPortfolioByUserId(@PathVariable userId: Int): Portfolio
}