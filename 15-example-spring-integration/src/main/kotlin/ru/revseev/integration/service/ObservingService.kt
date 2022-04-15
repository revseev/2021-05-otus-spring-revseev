@file:Suppress("unused")

package ru.revseev.integration.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.revseev.integration.domain.ManagedFile

private val log = KotlinLogging.logger { }


@Service
class ObservingService {

    fun observe(managedFile: ManagedFile): ManagedFile {
        log.info { "Observing file: $managedFile" }
        return managedFile
    }
}