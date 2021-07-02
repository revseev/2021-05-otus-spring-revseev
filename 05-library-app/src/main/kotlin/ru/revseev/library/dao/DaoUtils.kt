package ru.revseev.library.dao

import org.springframework.dao.support.DataAccessUtils
import ru.revseev.library.exception.DaoException

inline fun <reified T> wrapExceptions(errorMessage: String, action: () -> T?): T {
    return try {
        action.invoke()!!
    } catch (ex: Exception) {
        throw DaoException(errorMessage, ex)
    }
}

inline fun <reified T> wrapExceptionsNullable(errorMessage: String, action: () -> T?): T? {
    return try {
        action.invoke()
    } catch (ex: Exception) {
        throw DaoException(errorMessage, ex)
    }
}

fun <T> Collection<T>?.nullableSingleResult(): T? {
    return DataAccessUtils.nullableSingleResult(this)
}

fun <T> Collection<T>?.uniqueResult(): T? {
    return DataAccessUtils.uniqueResult(this)
}