package ru.revseev.integration

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

@SpringBootTest
@ActiveProfiles("test")
internal class IntegrationAppTest {

    @Value("\${paths.directory.destination}")
    lateinit var destinationRoot: String

    @Test
    fun `should load files from source, evaluate and copy NORMAL and COMPRESSIBLE types to deetination`() {
        TimeUnit.SECONDS.sleep(2)

        val list = Paths.get(destinationRoot).listDirectoryEntries()

        expectThat(list.map { it.name }).containsExactlyInAnyOrder(listOf("test-arch.rar", "test-xml.zip"))
    }
}