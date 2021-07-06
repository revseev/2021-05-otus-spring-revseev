package ru.revseev.library.service

import ru.revseev.library.domain.Author

interface AuthorService {

    fun getAll(): List<Author>

    fun getById(id: Long): Author

    fun add(author: Author): Long

    fun update(author: Author): Boolean

    fun deleteById(id: Long): Boolean
}
