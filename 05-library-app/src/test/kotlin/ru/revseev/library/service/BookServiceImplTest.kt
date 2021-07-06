package ru.revseev.library.service

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.revseev.library.*
import ru.revseev.library.exception.LibraryItemNotFoundException
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.service.impl.BookServiceImpl
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

@ExtendWith(MockKExtension::class)
internal class BookServiceImplTest {

    @MockK
    private val bookRepo: BookRepo = mockk()
    private lateinit var bookService: BookService

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

    @Nested
    inner class GetById {

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
        fun `getById() should throw LibraryItemNotFoundException when wrong id`() {
            every { bookRepo.findById(nonExistingId) } returns null

            expectThrows<LibraryItemNotFoundException> { bookService.getById(nonExistingId) }
        }
    }

    @Test
    fun `add() should pass the book to dao`() {
        val newBook = book1
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