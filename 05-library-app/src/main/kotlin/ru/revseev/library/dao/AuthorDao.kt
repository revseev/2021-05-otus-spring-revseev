package ru.revseev.library.dao

import ru.revseev.library.domain.Author


interface AuthorDao {

    fun getAll(): List<Author>

    fun getById(id: Long): Author

    fun add(author: Author): Boolean

    fun update(author: Author): Boolean

    fun deleteById(id: Long): Boolean

}