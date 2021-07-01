package ru.revseev.library.service

import ru.revseev.library.domain.Book

interface BookService {

    fun getAll(): List<Book>

}
