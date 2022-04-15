package ru.revseev.library.view.controller

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ru.revseev.library.*
import ru.revseev.library.service.BookService
import ru.revseev.library.service.CommentService
import ru.revseev.library.service.GenreService
import ru.revseev.library.view.BookDtoConverter
import ru.revseev.library.view.CommentDtoConverter
import ru.revseev.library.view.dto.BookDto
import ru.revseev.library.view.dto.GenreDto
import ru.revseev.library.view.impl.BookDtoConverterImpl
import ru.revseev.library.view.impl.CommentDtoConverterImpl
import ru.revseev.library.view.impl.GenreParserImpl

@WebMvcTest(BookViewController::class)
@ExtendWith(MockKExtension::class)
@Import(BookDtoConverterImpl::class, GenreParserImpl::class, CommentDtoConverterImpl::class)
@AutoConfigureMockMvc(addFilters = false)
internal class BookViewControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var bookService: BookService
    @MockkBean
    lateinit var commentService: CommentService
    @MockkBean
    lateinit var genreService: GenreService
    @SpykBean
    lateinit var bookDtoConverter: BookDtoConverter
    @SpykBean
    lateinit var commentDtoConverter: CommentDtoConverter

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @Test
    fun `allBooks() should return a view containing all books`() {
        val allBooks = listOf(book1, book2, book3)
        every { bookService.getAll() } returns allBooks
        val expected: List<BookDto> = allBooks.map { bookDtoConverter.toDto(it) }

        mockMvc.get("/book/all")
            .andExpect {
                status { isOk() }
                content { contentType("text/html;charset=UTF-8") }
                view { name("books") }
                model { attribute("allBooks", expected) }
            }
    }

    @Test
    fun `delete() should invoke deletion of the book and redirect to allBooks view`() {
        val id = book1.id
        mockMvc.get("/book/delete?id=$id")
            .andExpect {
                status { is3xxRedirection() }
                redirectedUrl("/book/all")
            }
        verify(exactly = 1) { bookService.deleteById(id) }
    }

    @Test
    fun `toEditForm() should return edit view with provided books' properties`() {
        val id = book1.id
        every { bookService.getById(id) } returns book1
        val expected = bookDtoConverter.toDto(book1)

        mockMvc.get("/book/edit?id=$id")
            .andExpect {
                status { isOk() }
                content { contentType("text/html;charset=UTF-8") }
                view { name("form") }
                model {
                    attribute("bookDto", expected)
                    attribute("edit", true)
                }
            }
    }

    @Test
    fun `toAddForm() should return new book view`() {
        val expected = BookDto()

        mockMvc.get("/book/new")
            .andExpect {
                status { isOk() }
                content { contentType("text/html;charset=UTF-8") }
                view { name("form") }
                model {
                    attribute("bookDto", expected)
                    attribute("edit", false)
                }
            }
    }

    @Test
    fun `update() should update given book and redirect to allBooks view`() {
        val bookDto = bookDtoConverter.toDto(book1)
            .apply {
                genres = mutableListOf(GenreDto("CHANGED GENRE"))
            }
        val expected = bookDtoConverter.toUpdatedBookDto(bookDto)

        mockMvc
            .post("/book/update") {
                flashAttr("bookDto", bookDto)
            }
            .andExpect {
                status { is3xxRedirection() }
                redirectedUrl("/book/all")
            }

        verify(exactly = 1) { bookService.update(expected) }
    }

    @Test
    fun `add() should add a new book and redirect to allBooks view`() {
        val bookDto = BookDto(
            title = "NEW TITLE",
            authorName = "NEW AUTHOR",
            genres = mutableListOf(GenreDto("NEW_GENRE_1"), GenreDto("NEW_GENRE_2"))
        )
        val expected = bookDtoConverter.toNewBookDto(bookDto)

        mockMvc
            .post("/book/add") {
                flashAttr("bookDto", bookDto)
            }
            .andExpect {
                status { is3xxRedirection() }
                redirectedUrl("/book/all")
            }

        verify(exactly = 1) { bookService.add(expected) }
    }

    @Test
    fun `commentsByBookId() should return a view with all comments of the book`() {
        val id = book2.id
        val comments = mutableListOf(comment21, comment22)
        val expected = comments.map { commentDtoConverter.toDto(it) }
        every { commentService.getByBookId(id) } returns comments

        mockMvc.get("/book/comments?id=$id")
            .andExpect {
                status { isOk() }
                view { name("comments") }
                model { attribute("comments", expected) }
            }
    }
}