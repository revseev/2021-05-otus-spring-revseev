package ru.revseev.library.service

import ru.revseev.library.domain.Book

interface BookService {

    fun getAll(): List<Book>

    fun getById(id: Long): Book

    fun add(book: Book): Long

    fun update(book: Book): Boolean

    fun deleteById(id: Long): Boolean
}
