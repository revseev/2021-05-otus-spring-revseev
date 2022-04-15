package ru.revseev.integration.domain

data class ManagedFile(
    val path: String,
    val fileName: String,
    val extension: String,
    val type: ManagedType,
)

enum class ManagedType {
    NORMAL, COMPRESSIBLE, IGNORED
}
