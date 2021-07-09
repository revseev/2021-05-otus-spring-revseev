package ru.revseev.library.repo

import ru.revseev.library.domain.Book

interface BookRepo {

    fun findAll(): List<Book>

    fun findById(id: Long): Book?

    fun save(book: Book): Book

    fun deleteById(id: Long): Boolean
}