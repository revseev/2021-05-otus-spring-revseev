package ru.revseev.library.view.controller

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.revseev.library.service.ReportService
import ru.revseev.library.service.impl.ReportServiceImpl
import ru.revseev.library.view.dto.Report

private val log = KotlinLogging.logger { }

@RestController
class ReportController(val reportService: ReportServiceImpl) {

    @GetMapping("reports/new")
    fun getReport(): Report {
        val report = reportService.getReport()
        log.info { "Got Report: $report" }
        return report
    }
}

