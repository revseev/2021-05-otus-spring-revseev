@file:Suppress("unused")

package ru.revseev.integration.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.revseev.integration.domain.ManagedFile
import ru.revseev.integration.domain.ManagedType
import java.io.File

private val log = KotlinLogging.logger { }

@Service
class FileService {

    fun analyze(file: File): ManagedFile {
        log.info { "Analyzing file: $file" }
        return ManagedFile(
            path = file.absolutePath,
            fileName = file.nameWithoutExtension,
            extension = file.extension.lowercase(),
            type = when (file.extension.lowercase()) {
                "mpg", "mp4", "zip", "rar" -> ManagedType.NORMAL
                "txt", "xml", "csv", "doc" -> ManagedType.COMPRESSIBLE
                else -> ManagedType.IGNORED
            }
        )
    }
}