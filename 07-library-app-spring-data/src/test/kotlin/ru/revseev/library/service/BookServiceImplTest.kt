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
import org.springframework.data.repository.findByIdOrNull
import ru.revseev.library.book1
import ru.revseev.library.exception.LibraryItemNotFoundException
import ru.revseev.library.existingId1
import ru.revseev.library.nonExistingId
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.service.impl.BookServiceImpl
import ru.revseev.library.shell.dto.GenreDto
import ru.revseev.library.shell.dto.NewBookDto
import ru.revseev.library.shell.dto.UpdatedBookDto
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

@Suppress("UnusedEquals") // cannot use equals on entities without ids
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
            every { bookRepo.findByIdOrNull(id) } returns expected

            val actual = bookService.getById(id)
            verify(exactly = 1) { bookRepo.findById(id) }
            expectThat(actual).isEqualTo(expected)
        }

        @Test
        fun `getById() should throw LibraryItemNotFoundException when wrong id`() {
            every { bookRepo.findByIdOrNull(nonExistingId) } returns null

            expectThrows<LibraryItemNotFoundException> { bookService.getById(nonExistingId) }
        }
    }

    @Test
    fun `add() should create Book from Dto and pass it to Repo`() {
        val bookDto = NewBookDto("Book1", "Author1", listOf(GenreDto("Genre1"), GenreDto("Genre2")))
        bookService.add(bookDto)

        verify {
            bookRepo.save(withArg {
                bookDto.title == it.title
                bookDto.authorName == it.author.name
                bookDto.genres.forEachIndexed { i, expGenre -> expGenre.name == it.genres[i].name }
            })
        }
    }

    @Test
    fun `update() should pass the book to dao`() {
        every { bookService.getById(existingId1) } returns book1

        val bookDto = UpdatedBookDto(existingId1, listOf(GenreDto("newGenre1"), GenreDto("newGenre2")))
        bookService.update(bookDto)

        verify {
            bookRepo.save(withArg {
                bookDto.id == it.id
                bookDto.genres.forEachIndexed { i, expGenre -> expGenre.name == it.genres[i].name }
            })
        }
    }

    @Test
    fun `deleteById() should pass the id to dao`() {
        val id = existingId1
        bookService.deleteById(id)

        verify { bookRepo.deleteBookById(id) }
    }
}