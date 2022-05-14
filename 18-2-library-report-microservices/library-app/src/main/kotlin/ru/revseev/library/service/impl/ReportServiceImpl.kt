package ru.revseev.library.service.impl

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.revseev.library.exception.NoCachedReportException
import ru.revseev.library.service.ReportService
import ru.revseev.library.service.feign.ReportClient
import ru.revseev.library.view.dto.Report
import java.util.concurrent.ConcurrentHashMap

private val log = KotlinLogging.logger { }
private const val LATEST = "latest"

@Service
class ReportServiceImpl(val reportClient: ReportClient) : ReportService {

    private val reportCache: MutableMap<String, Report> = ConcurrentHashMap<String, Report>(1)
    private val dummyReport = Report("We cannot generate a report or use a cached copy at the moment", null, null)

    @HystrixCommand(commandKey = "generateReport", fallbackMethod = "getLatestCachedReport")
    override fun getReport(): Report {
        log.info { "[GET: reports/new] Calling ReportClient for Report..." }
        val report = reportClient.generateReport()
        log.info { "Caching latest report" }
        reportCache[LATEST] = report.copy(data = "Cached version of: ${report.data}")
        return report
    }

    @HystrixCommand(commandKey = "generateReport", fallbackMethod = "getDummyReport")
    fun getLatestCachedReport(): Report {
        log.info { "Returning latest cached report" }
        return try {
            reportCache[LATEST]!!
        } catch (ex: Exception) {
            throw NoCachedReportException(ex)
        }
    }

    fun getDummyReport(): Report {
        log.info { "Returning dummy report" }
        return dummyReport
    }
}