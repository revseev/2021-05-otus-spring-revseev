package ru.revseev.library.view.impl

import org.springframework.stereotype.Component
import ru.revseev.library.domain.Book
import ru.revseev.library.view.BookDtoConverter
import ru.revseev.library.view.dto.BookDto
import ru.revseev.library.view.dto.NewBookDto
import ru.revseev.library.view.dto.UpdatedBookDto
import ru.revseev.library.view.dto.toDto

@Component
class BookDtoConverterImpl : BookDtoConverter {

    override fun toDto(book: Book): BookDto = BookDto(
        id = book.id,
        title = book.title,
        authorName = book.author.name,
        genres = book.genres.mapTo(mutableListOf()) { it.toDto() }
    )

    override fun toNewBookDto(bookDto: BookDto): NewBookDto = NewBookDto(
        title = bookDto.title,
        authorName = bookDto.authorName,
        genres = bookDto.genres)

    override fun toUpdatedBookDto(bookDto: BookDto): UpdatedBookDto {
        val updatedBookId: String = bookDto.id
            ?: throw IllegalStateException("Wrong method use! BookDto must have an id to be converted to UpdatedBookDto")
        return UpdatedBookDto(updatedBookId, bookDto.genres)
    }
}