package ru.revseev.library.service

import ru.revseev.library.domain.Book
import ru.revseev.library.shell.dto.NewBookDto
import ru.revseev.library.shell.dto.UpdatedBookDto

interface BookService {

    fun getAll(): List<Book>

    fun getById(id: Long): Book

    fun add(dto: NewBookDto): Book

    fun update(dto: UpdatedBookDto): Book

    fun deleteById(id: Long): Boolean

    fun getBookWithCommentsById(id: Long): Book
}
