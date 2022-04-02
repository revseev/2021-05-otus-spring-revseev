package ru.revseev.library.batch

import mu.KotlinLogging
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.io.FileNotFoundException

private val log = KotlinLogging.logger { }


@Service
class SqlExecutioner(
    private val jdbc: JdbcTemplate,
) {

    @Suppress("unused")
    fun execute(sqlResourceFilename: String) {
        log.info { "Executing .sql file" }
        jdbc.execute(sqlResourceFilename.resourceAsText())
    }
}

fun String.resourceAsText(): String =
    Thread.currentThread().contextClassLoader.getResourceAsStream(this)?.bufferedReader()?.readText()
        ?: throw FileNotFoundException("Failed to get resource at '$this'")

