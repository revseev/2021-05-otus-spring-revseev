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
import ru.revseev.library.service.impl.BookServiceImpl
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExtendWith(MockKExtension::class)
internal class BookServiceImplTest {

    @MockK
    private val dao: BookDao = mockk()
    private lateinit var bookService: BookService

    private val genre1 = Genre(1L, "Genre1")
    private val author1 = Author(1L, "Author1")
    private val book1 = Book(1L, "Book1", author1, mutableListOf(genre1))

    @BeforeEach
    fun resetMocks() {
        clearAllMocks()
        bookService = BookServiceImpl(dao)
    }

    @Test
    fun `getAll() should return exactly what dao return`() {
        val expected = listOf(book1)
        every { dao.getAll() } returns expected

        val actual = bookService.getAll()
        verify(exactly = 1) { dao.getAll() }
        expectThat(actual).isEqualTo(expected)
    }

    @Test
    fun `getById() should return exactly what dao return`() {
        val id = 1L
        val expected = book1
        every { dao.getById(id) } returns expected

        val actual = bookService.getById(id)
        verify(exactly = 1) { dao.getById(id) }
        expectThat(actual).isEqualTo(expected)
    }

    @Test
    fun `add() should pass the book to dao`() {
        val newBook = book1.copy(id = null)
        bookService.add(newBook)

        verify { dao.add(newBook) }
    }

    @Test
    fun `update() should pass the book to dao`() {
        bookService.update(book1)

        verify { dao.update(book1) }
    }

    @Test
    fun `deleteById() should pass the id to dao`() {
        val id = 1L
        bookService.deleteById(id)

        verify { dao.deleteById(id) }
    }
}