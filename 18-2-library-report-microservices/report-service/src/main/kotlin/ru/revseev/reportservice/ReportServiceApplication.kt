package ru.revseev.reportservice

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private val log = KotlinLogging.logger { }

@SpringBootApplication
class ReportServiceApplication

fun main(args: Array<String>) {
    runApplication<ReportServiceApplication>(*args)
}

@RestController
class UnreliableReportController(
    @Value("\${report.precision}") val precision: String,
) {

    @GetMapping("generate")
    fun generate(): Report {
        val timeToGenerate: Long = if (Random.nextBoolean()) 1 else 10
        log.info { "Estimated time to generate report: $timeToGenerate seconds" }
        TimeUnit.SECONDS.sleep(timeToGenerate)

        return Report(data = "Accurate and timely report!",
            generatedTime = "$timeToGenerate seconds",
            precision = precision)
    }
}

data class Report(val data: String, val generatedTime: String?, val precision: String?)