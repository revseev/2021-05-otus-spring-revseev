@file:Suppress("unused")

package ru.revseev.integration.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.revseev.integration.domain.ManagedFile
import java.io.*
import java.io.File.separator
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.absolutePathString
import kotlin.io.path.copyTo
import kotlin.io.path.createDirectories

private val log = KotlinLogging.logger { }
private const val ZIP_EXTENSION = "zip"

@Service
class CopyService {

    @Value("\${paths.directory.destination}")
    lateinit var destinationRoot: String

    fun copyAndCompress(original: ManagedFile): ManagedFile {
        log.info { "Compressing file: $original" }
        val file = File(original.path)
        val archive = File(destinationRoot + separator + original.fileName + "." + ZIP_EXTENSION)
        zip(file, archive)
        return original.copy(path = archive.absolutePath, extension = ZIP_EXTENSION)
    }

    fun copy(original: ManagedFile): ManagedFile {
        log.info { "Copying file: $original" }
        val path = Paths.get(original.path)
        val copied = path.copyTo(Paths.get(destinationRoot + separator + path.fileName).apply {
            parent?.createDirectories()
        }, overwrite = true)
        return original.copy(path = copied.absolutePathString())
    }

}

fun zip(from: File, toZip: File) {
    zip(listOf(from), toZip)
}

fun zip(fromFiles: List<File>, toZip: File) {
    ZipOutputStream(BufferedOutputStream(FileOutputStream(toZip))).use { output ->
        fromFiles.forEach { file ->
            (file.length() > 1).ifTrue {
                FileInputStream(file).use { input ->
                    BufferedInputStream(input).use { origin ->
                        val entry = ZipEntry(file.name)
                        output.putNextEntry(entry)
                        origin.copyTo(output, 1024)
                    }
                }
            }
        }
    }
}

private fun Boolean.ifTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}
