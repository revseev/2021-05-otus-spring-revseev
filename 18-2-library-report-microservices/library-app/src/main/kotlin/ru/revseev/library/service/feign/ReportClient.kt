package ru.revseev.library.service.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import ru.revseev.library.view.dto.Report

@FeignClient(name = "report-service")
interface ReportClient {

    @GetMapping("generate")
    fun generateReport(): Report
}