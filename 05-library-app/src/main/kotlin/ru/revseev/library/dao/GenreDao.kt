package ru.revseev.library.dao

import ru.revseev.library.domain.Genre


interface GenreDao {

    fun getAll(): List<Genre>

    fun getById(id: Long): Genre

    fun add(genre: Genre): Boolean

    fun update(genre: Genre): Boolean

    fun deleteById(id: Long): Boolean

}