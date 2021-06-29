package ru.revseev.library.dao

import ru.revseev.library.domain.Book


interface BookDao {

    fun getAll(): List<Book>

    fun getById(id: Long): Book

    fun add(book: Book): Book

    fun update(book: Book): Book

    fun deleteById(id: Long)

}