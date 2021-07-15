package ru.revseev.library.service.impl

import ru.revseev.library.exception.RepositoryException

inline fun <reified T> wrapExceptions(errorMessage: String? = null, action: () -> T): T {
    return try {
        action.invoke()
    } catch (ex: Exception) {
        when (errorMessage) {
            null -> throw RepositoryException(ex)
            else -> throw RepositoryException(errorMessage, ex)
        }
    }
}