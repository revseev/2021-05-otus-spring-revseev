package ru.revseev.library.service

import ru.revseev.library.domain.Book

interface BookService {

    fun getAll(): List<Book>

    fun getById(id: Long): Book

    fun add(book: Book): Book

    fun update(book: Book): Book

    fun deleteById(id: Long): Boolean
}
