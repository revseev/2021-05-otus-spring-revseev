package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import ru.revseev.library.domain.Book
import ru.revseev.library.exception.LibraryItemNotFoundException
import ru.revseev.library.exception.RepositoryException
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.service.BookService

@Service
class BookServiceImpl(private val bookRepo: BookRepo) : BookService {

    override fun getAll(): List<Book> = wrapExceptions { bookRepo.findAll() }

    override fun getById(id: Long): Book = wrapExceptions {
        bookRepo.findById(id)
    } ?: throw LibraryItemNotFoundException("Book with id = $id was not found")

    override fun update(book: Book): Book = wrapExceptions { bookRepo.save(book) }

    override fun add(book: Book): Book = wrapExceptions { bookRepo.save(book) }

    override fun deleteById(id: Long): Boolean = wrapExceptions { bookRepo.deleteById(id) }
}