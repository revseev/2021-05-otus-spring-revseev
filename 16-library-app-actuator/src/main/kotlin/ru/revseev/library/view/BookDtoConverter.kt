package ru.revseev.library.view

import ru.revseev.library.domain.Book
import ru.revseev.library.view.dto.BookDto
import ru.revseev.library.view.dto.NewBookDto
import ru.revseev.library.view.dto.UpdatedBookDto

interface BookDtoConverter {

    fun toDto(book: Book): BookDto

    fun toNewBookDto(bookDto: BookDto): NewBookDto

    fun toUpdatedBookDto(bookDto: BookDto): UpdatedBookDto
}