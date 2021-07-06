package ru.revseev.library.service

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.revseev.library.dao.BookDao
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.repo.existingId1
import ru.revseev.library.service.impl.BookServiceImpl
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExtendWith(MockKExtension::class)
internal class BookServiceImplTest {

    @MockK
    private val bookRepo: BookRepo = mockk()
    private lateinit var bookService: BookService

    private val genre1 = Genre("Genre1").apply { id = existingId1 }
    private val author1 = Author("Author1").apply { id = existingId1 }
    private val book1 = Book("Book1", author1, mutableListOf(genre1)).apply { id = existingId1 }

    @BeforeEach
    fun resetMocks() {
        clearAllMocks()
        bookService = BookServiceImpl(bookRepo)
    }

    @Test
    fun `getAll() should return exactly what dao return`() {
        val expected = listOf(book1)
        every { bookRepo.findAll() } returns expected

        val actual = bookService.getAll()
        verify(exactly = 1) { bookRepo.findAll() }
        expectThat(actual).isEqualTo(expected)
    }

    @Test
    fun `getById() should return exactly what dao return`() {
        val id = existingId1
        val expected = book1
        every { bookRepo.findById(id) } returns expected

        val actual = bookService.getById(id)
        verify(exactly = 1) { bookRepo.findById(id) }
        expectThat(actual).isEqualTo(expected)
    }

    @Test
    fun `add() should pass the book to dao`() {
        val newBook = Book("Book1", author1, mutableListOf(genre1))
        bookService.add(newBook)

        verify { bookRepo.save(newBook) }
    }

    @Test
    fun `update() should pass the book to dao`() {
        bookService.update(book1)

        verify { bookRepo.save(book1) }
    }

    @Test
    fun `deleteById() should pass the id to dao`() {
        val id = existingId1
        bookService.deleteById(id)

        verify { bookRepo.deleteById(id) }
    }
}