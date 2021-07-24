package ru.revseev.library.domain

import org.springframework.data.annotation.Id

abstract class LongIdentifiable {
    @Id // todo remove
    var id: Long? = null

    fun isNew(): Boolean = id == null
}