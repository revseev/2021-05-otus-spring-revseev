package ru.revseev.library.service

import ru.revseev.library.domain.Book
import ru.revseev.library.view.dto.NewBookDto
import ru.revseev.library.view.dto.UpdatedBookDto

interface BookService {

    fun getAll(): List<Book>

    fun getById(id: String): Book

    fun add(dto: NewBookDto): Book

    fun update(dto: UpdatedBookDto): Book

    fun deleteById(id: String): Boolean
}
