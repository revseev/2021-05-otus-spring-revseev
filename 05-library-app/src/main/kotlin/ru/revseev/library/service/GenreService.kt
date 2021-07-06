package ru.revseev.library.service

import ru.revseev.library.domain.Genre


interface GenreService {

    fun getAll(): List<Genre>

    fun getById(id: Long): Genre

    fun getByName(name: String): Genre?

    fun add(genre: Genre): Long

    fun update(genre: Genre): Boolean

    fun deleteById(id: Long): Boolean
}