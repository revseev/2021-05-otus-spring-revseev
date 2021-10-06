package ru.revseev.library.view.impl

import org.springframework.stereotype.Component
import ru.revseev.library.domain.Book
import ru.revseev.library.exception.InvalidEntityException
import ru.revseev.library.view.BookDtoConverter
import ru.revseev.library.view.GenreParser
import ru.revseev.library.view.dto.BookDto
import ru.revseev.library.view.dto.NewBookDto
import ru.revseev.library.view.dto.UpdatedBookDto

@Component
class BookDtoConverterImpl(val genreParser: GenreParser) : BookDtoConverter {

    override fun toDto(book: Book): BookDto = BookDto(
        id = book.id,
        title = book.title,
        authorName = book.author.name,
        genres = book.genres.joinToString(", ") { it.name }
    )

    override fun toNewBookDto(bookDto: BookDto): NewBookDto = NewBookDto(
        title = bookDto.title,
        authorName = bookDto.authorName,
        genres = genreParser.parseGenres(bookDto.genres))

    override fun toUpdatedBookDto(bookDto: BookDto): UpdatedBookDto {
        val updatedBookId: String = bookDto.id
            ?: throw InvalidEntityException("BookDto must have an id to be converted to UpdatedBookDto")
        return UpdatedBookDto(updatedBookId, genreParser.parseGenres(bookDto.genres))
    }
}