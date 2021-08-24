package ru.revseev.library.service.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.domain.Sort
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
    override suspend fun getAll(): Flow<Book> = bookRepo.findAll().asFlow()

    @Transactional(readOnly = true)
    override suspend fun getAll(sort: Sort): Flow<Book> = bookRepo.findAll(sort).asFlow()

    @Transactional(readOnly = true)
    override suspend fun getById(id: String): Book = bookRepo.findById(id).awaitFirstOrElse {
        throw LibraryItemNotFoundException("Book with id = $id was not found")
    }

    @Transactional
    override suspend fun add(dto: NewBookDto): Book {
        val newBook = Book(dto.title, Author(dto.authorName), dto.genres.toGenres().toMutableList())
        return bookRepo.save(newBook).awaitSingle()
    }

    @Transactional
    override suspend fun update(dto: UpdatedBookDto): Book {
        val book = getById(dto.id).apply {
            this.genres = dto.genres.toGenres().toMutableList()
        }
        return bookRepo.save(book).awaitSingle()
    }

    @Transactional
    override suspend fun deleteById(id: String): Boolean =
        try {
            bookRepo.deleteById(id).awaitSingle()
            true
        } catch (ex: Exception) {
            false
        }
}