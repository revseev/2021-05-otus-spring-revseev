package ru.revseev.library.dao

import ru.revseev.library.domain.Genre


interface GenreDao {

    fun getAll(): List<Genre>

    fun getById(id: Long): Genre

    fun add(genre: Genre): Genre

    fun update(genre: Genre): Genre

    fun deleteById(id: Long)

}