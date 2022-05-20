package com.revseev.diversify.visualizer.service

import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "diversify-core")
interface CoreClient {
}