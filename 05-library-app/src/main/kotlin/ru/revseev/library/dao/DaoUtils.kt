package ru.revseev.library.dao

import ru.revseev.library.exception.DaoException

inline fun <reified T> wrapExceptions(errorMessage: String, action: () -> T?): T {
    return try {
        action.invoke()!!
    } catch (ex: Exception) {
        throw DaoException(errorMessage, ex)
    }
}