package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.domain.Book
import ru.revseev.library.exception.LibraryItemNotFoundException
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.service.BookService

@Service
class BookServiceImpl(private val bookRepo: BookRepo) : BookService {

    @Transactional(readOnly = true)
    override fun getAll(): List<Book> = wrapExceptions { bookRepo.findAll() }

    @Transactional(readOnly = true)
    override fun getById(id: Long): Book = wrapExceptions {
        bookRepo.findById(id)
    } ?: throw LibraryItemNotFoundException("Book with id = $id was not found")

    @Transactional
    override fun update(book: Book): Book = wrapExceptions { bookRepo.save(book) }

    @Transactional
    override fun add(book: Book): Book = wrapExceptions { bookRepo.save(book) }

    @Transactional
    override fun deleteById(id: Long): Boolean = wrapExceptions { bookRepo.deleteById(id) }
}