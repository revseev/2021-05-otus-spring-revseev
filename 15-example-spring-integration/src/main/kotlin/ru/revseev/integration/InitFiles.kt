package ru.revseev.integration

import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories

private val log = KotlinLogging.logger { }


@Profile("test")
@Component
class InitFiles : InitializingBean {

    @Value("\${paths.directory.source}")
    lateinit var sourceRoot: String

    override fun afterPropertiesSet() {
        log.info { "[INIT] Creating source dir" }
        val sourceDir = Paths.get(sourceRoot).createDirectories()
        sourceDir.createStubFile("test-arch.rar")
        sourceDir.createStubFile("test-readme.md")
        sourceDir.createStubFile("test-xml.xml")
    }

    private fun Path.createStubFile(filename: String) {
        log.info { "[INIT] Creating test file: $filename" }
        try {
            Files.createFile(resolve(filename))
        } catch (ignored: FileAlreadyExistsException) {
        }
    }
}