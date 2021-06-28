package ru.revseev.library.shell

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.shell.Input
import org.springframework.shell.Shell
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import ru.revseev.library.service.BookService
import ru.revseev.library.service.GenreService


@SpringBootTest
internal class AdminLibraryShellTest {

    @Autowired
    lateinit var shell: Shell

    @SpykBean
    lateinit var bookService: BookService
    @SpykBean
    lateinit var genreService: GenreService

    @MockkBean
    lateinit var bookViewer: BookViewer
    @MockkBean
    lateinit var genreViewer: GenreViewer
    @MockkBean
    lateinit var genreParser: GenreParser

    @Test
    fun `given shell command 'books' should getAllBooks via BookService`() {
        shell.evaluate { "books" }

        verify(exactly = 1) { bookService.getAll() }
    }

    @Test
    fun `should view books via BookViewer`() {
        val expected = bookService.getAll()
        shell.evaluate { "books" }

        verify(exactly = 1) { bookViewer.viewList(expected) }
    }

    @Test
    fun `should view genres via GenreViewer`() {
        val expected = genreService.getAll()
        shell.evaluate { "genres" }

        verify(exactly = 1) { genreViewer.viewList(expected) }
    }

    @Test
    fun `when add a book without genres, a book with empty genre list is created`() {
        shell.evaluate { "ba title author" }

        val expected = Book(title = "title", author = Author(name = "author"))
        verify { bookService.add(expected) }
    }

    @Test
    fun `when given a book with genres, they are parsed using GenreParser`() {
        val genresString = "genre1, genre2"
        val expectedGenres = mutableListOf(Genre(name = "genre1"), Genre(name = "genre2"))
        every { genreParser.parseGenres(genresString) } returns expectedGenres

        shell.evaluate(TestInputWithSpaces(mutableListOf("ba", "new book title", "some author", genresString)))

        val expected = Book(
            title = "new book title",
            author = Author(name = "some author"),
            genres = expectedGenres
        )
        verify {
            genreParser.parseGenres(genresString)
            bookService.add(expected)
        }
    }
}

class TestInputWithSpaces(private val words: MutableList<String>) : Input {

    override fun rawText(): String = words.joinToString { " " }

    override fun words(): MutableList<String> = words
}