package ru.revseev.library.service.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.exception.LibraryItemNotFoundException
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.service.BookService
import ru.revseev.library.shell.dto.NewBookDto
import ru.revseev.library.shell.dto.UpdatedBookDto
import ru.revseev.library.shell.dto.toGenres

@Service
class BookServiceImpl(private val bookRepo: BookRepo) : BookService {

    @Transactional(readOnly = true)
    override fun getAll(): List<Book> = wrapExceptions { bookRepo.findAll() }

    @Transactional(readOnly = true)
    override fun getById(id: Long): Book = wrapExceptions {
        bookRepo.findByIdOrNull(id)
    } ?: throw LibraryItemNotFoundException("Book with id = $id was not found")

    @Transactional
    override fun update(dto: UpdatedBookDto): Book {
        val book = getById(dto.id).apply { genres = dto.genres.toGenres() }
        return wrapExceptions {
            bookRepo.save(book)
        }
    }

    @Transactional
    override fun add(dto: NewBookDto): Book = wrapExceptions {
        bookRepo.save(Book(dto.title, Author(dto.authorName), dto.genres.toGenres()))
    }

    @Transactional
    override fun deleteById(id: Long): Boolean = wrapExceptions { bookRepo.deleteBookById(id) }
}