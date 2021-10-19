package ru.revseev.library.service.impl

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.exception.LibraryItemNotFoundException
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.service.BookService
import ru.revseev.library.view.dto.NewBookDto
import ru.revseev.library.view.dto.UpdatedBookDto
import ru.revseev.library.view.dto.toGenres

@Service
class BookServiceImpl(
    private val bookRepo: BookRepo,
) : BookService {

    @Transactional(readOnly = true)
    override fun getAll(): List<Book> = bookRepo.findAll()

    @Transactional(readOnly = true)
    override fun getAll(pageable: Pageable): List<Book> = bookRepo.findAll(pageable).content

    @Transactional(readOnly = true)
    override fun getById(id: String): Book = bookRepo.findById(id).orElseThrow {
        LibraryItemNotFoundException("Book with id = $id was not found")
    }

    @Transactional
    override fun add(dto: NewBookDto): Book {
        val newBook = Book(dto.title, Author(dto.authorName), dto.genres.toGenres().toMutableList())
        return bookRepo.save(newBook)
    }

    @Transactional
    override fun update(dto: UpdatedBookDto): Book {
        val book = getById(dto.id).apply {
            this.genres = dto.genres.toGenres().toMutableList()
        }
        return bookRepo.save(book)
    }

    @Transactional
    override fun deleteById(id: String): Boolean =
        try {
            bookRepo.deleteById(id)
            true
        } catch (ex: Exception) {
            false
        }
}