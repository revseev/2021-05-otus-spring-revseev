package ru.revseev.library.shell

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.shell.Input
import org.springframework.shell.Shell
import ru.revseev.library.comment11
import ru.revseev.library.comment12
import ru.revseev.library.service.BookService
import ru.revseev.library.service.CommentService
import ru.revseev.library.service.GenreService
import ru.revseev.library.shell.dto.GenreDto
import ru.revseev.library.shell.dto.NewBookDto


@SpringBootTest
internal class AdminLibraryShellTest {

    @Autowired
    lateinit var shell: Shell

    @SpykBean
    lateinit var bookService: BookService
    @SpykBean
    lateinit var genreService: GenreService

    @MockkBean
    lateinit var commentService: CommentService
    @MockkBean
    lateinit var bookViewer: BookViewer
    @MockkBean
    lateinit var genreViewer: GenreViewer
    @MockkBean
    lateinit var genreParser: GenreParser
    @MockkBean
    lateinit var commentViewer: CommentViewer

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

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

        val expected = NewBookDto("title", "author", emptyList())
        verify {
            bookService.add(expected)
        }
    }

    @Test
    fun `when given a book with genres, they are parsed using GenreParser`() {
        val genresString = "genre1, genre2"
        val expectedGenres = mutableListOf(GenreDto("genre1"), GenreDto("genre2"))
        every { genreParser.parseGenres(genresString) } returns expectedGenres

        shell.evaluate(TestInputWithSpaces(mutableListOf("ba", "new book title", "some author", genresString)))

        val expected = NewBookDto("new book title", "some author", expectedGenres)

        verify {
            genreParser.parseGenres(genresString)
            bookService.add(expected)
        }
    }

    @Nested
    inner class GetComments {

        @Test
        fun `should use CommentService to get CommentsByBookId`() {
            shell.evaluate { "bc 1" }

            verify { commentService.getByBookId(1L) }
        }

        @Test
        fun `should use CommentViewer to view comment list`() {
            val comments = mutableListOf(comment11, comment12)
            every { commentService.getByBookId(1L) } returns comments

            shell.evaluate { "bc 1" }

            verify { commentViewer.viewList(comments) }
        }
    }
}

class TestInputWithSpaces(private val words: MutableList<String>) : Input {

    override fun rawText(): String = words.joinToString { " " }

    override fun words(): MutableList<String> = words
}