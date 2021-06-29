package ru.revseev.library.dao

import org.springframework.dao.EmptyResultDataAccessException
import ru.revseev.library.exception.DataNotFoundException

inline fun <reified T> getNonNullable(errorMessage: String, action: () -> T?): T {
    val t: T? = try {
        action.invoke()
    } catch (ex: EmptyResultDataAccessException) {
        throw DataNotFoundException(errorMessage, ex)
    }
    return t ?: throw DataNotFoundException(errorMessage)
}