package ru.revseev.library.view.rest


import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.asFlow
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import ru.revseev.library.book1
import ru.revseev.library.book2
import ru.revseev.library.book3
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import ru.revseev.library.exception.LibraryItemNotFoundException
import ru.revseev.library.service.BookService
import ru.revseev.library.view.BookDtoConverter
import ru.revseev.library.view.dto.BookDto
import ru.revseev.library.view.impl.BookDtoConverterImpl
import ru.revseev.library.view.impl.GenreParserImpl


@WebMvcTest(BookRestController::class)
@ExtendWith(MockKExtension::class)
@Import(BookDtoConverterImpl::class, GenreParserImpl::class)
internal class BookRestControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var bookService: BookService
    @SpykBean
    lateinit var bookDtoConverter: BookDtoConverter

    @BeforeEach
    fun init() {
        clearAllMocks()
    }


    @Test
    fun `getAll() should return a list of BookDto with specified paging parameters`() {
        val offset = 0
        val limit = 2
        val pagedBooks = listOf(book3, book2)
        val sort = Sort.by(Sort.Direction.DESC, "id")

        coEvery {
            bookService.getAll(sort)
        } returns pagedBooks.asFlow()

        mockMvc.get("/api/v1/books?offset=$offset&limit=$limit")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$", hasSize<BookDto>(2))
                    jsonPath("$[0].title", `is`(book3.title))
                    jsonPath("$[1].title", `is`(book2.title))
                }
            }
    }

    @Nested
    inner class GetById {

        @Test
        fun `should return a valid BookDto`() {
            val id = book1.id
            val expected = bookDtoConverter.toDto(book1)

            coEvery { bookService.getById(id) } returns book1

            mockMvc.get("/api/v1/books/$id")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.id", `is`(expected.id))
                        jsonPath("$.title", `is`(expected.title))
                        jsonPath("$.authorName", `is`(expected.authorName))
                        jsonPath("$.genres", `is`(expected.genres))
                    }
                }
        }

        @Test
        fun `should return a 404 error if nothing is found`() {
            val id = "bad id"

            coEvery { bookService.getById(id) } throws LibraryItemNotFoundException("No book found with id = $id")

            mockMvc.get("/api/v1/books/$id")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content { emptyString() }
                }
        }
    }

    @Test
    fun `add() should add and return a new Book`() {
        val bookDto = BookDto(title = "TITLE", authorName = "AUTHOR", genres = "NEW GENRE1, NEW GENRE2")
        val newBookDto = bookDtoConverter.toNewBookDto(bookDto)
        val newBook = Book(
            bookDto.title,
            Author(bookDto.title),
            newBookDto.genres.mapTo(mutableListOf()) { Genre(it.name) }
        )
        val expected = bookDtoConverter.toDto(newBook)

        coEvery { bookService.add(newBookDto) } returns newBook

        mockMvc
            .post("/api/v1/books") {
                contentType = MediaType.APPLICATION_JSON
                content = """{
                        "id": null,
                        "title": "${bookDto.title}",
                        "authorName": "${bookDto.authorName}",
                        "genres": "${bookDto.genres}"
                    }""".trimIndent()
                accept = MediaType.APPLICATION_JSON
            }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$.id", `is`(expected.id))
                    jsonPath("$.title", `is`(expected.title))
                    jsonPath("$.authorName", `is`(expected.authorName))
                    jsonPath("$.genres", `is`(expected.genres))
                }
            }
    }

    @Nested
    inner class Update {

        @Test
        fun `should update and return a book if valid request`() {
            val bookDto = BookDto(
                id = book1.id,
                title = book1.title,
                authorName = book1.author.name,
                genres = "NEW GENRE1, NEW GENRE2"
            )
            val updatedBookDto = bookDtoConverter.toUpdatedBookDto(bookDto)
            val updatedBook = Book(
                bookDto.title,
                Author(bookDto.authorName),
                updatedBookDto.genres.mapTo(mutableListOf()) { Genre(it.name) }
            )
            val expected = bookDtoConverter.toDto(updatedBook)

            coEvery { bookService.update(updatedBookDto) } returns updatedBook

            mockMvc
                .put("/api/v1/books/${bookDto.id}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = """{
                        "id": "${bookDto.id}",
                        "title": "${bookDto.title}",
                        "authorName": "${bookDto.authorName}",
                        "genres": "${bookDto.genres}"
                    }""".trimIndent()
                    accept = MediaType.APPLICATION_JSON
                }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.id", `is`(expected.id))
                        jsonPath("$.title", `is`(expected.title))
                        jsonPath("$.authorName", `is`(expected.authorName))
                        jsonPath("$.genres", `is`(expected.genres))
                    }
                }

        }

        @Test
        fun `should return 400 if id in url not match id in body`() {
            val someId = "123"
            val bookDto = BookDto(
                id = book1.id,
                title = book1.title,
                authorName = book1.author.name,
                genres = "NEW GENRE1, NEW GENRE2"
            )

            mockMvc
                .put("/api/v1/books/${someId}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = """{
                        "id": "${bookDto.id}",
                        "title": "${bookDto.title}",
                        "authorName": "${bookDto.authorName}",
                        "genres": "${bookDto.genres}"
                    }""".trimIndent()
                    accept = MediaType.APPLICATION_JSON
                }
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }

            coVerify { bookService.update(any()) wasNot Called }
        }

        @Test
        fun `should return 404 if entity for update was not found`() {
            val someId = "123"
            val bookDto = BookDto(
                id = someId,
                title = book1.title,
                authorName = book1.author.name,
                genres = "NEW GENRE1, NEW GENRE2"
            )
            val updatedBookDto = bookDtoConverter.toUpdatedBookDto(bookDto)

            coEvery {
                bookService.update(updatedBookDto)
            } throws LibraryItemNotFoundException("Provided book was not found")

            mockMvc
                .put("/api/v1/books/${someId}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = """{
                        "id": "$someId",
                        "title": "${bookDto.title}",
                        "authorName": "${bookDto.authorName}",
                        "genres": "${bookDto.genres}"
                    }""".trimIndent()
                    accept = MediaType.APPLICATION_JSON
                }
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    inner class Delete {

        @Test
        fun `should return 200 on successful delete`() {
            val id = book1.id

            coEvery { bookService.deleteById(id) } returns true

            mockMvc.delete("/api/v1/books/$id")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                }
        }

        @Test
        fun `should return 404 on unsuccessful delete`() {
            val id = book1.id

            coEvery { bookService.deleteById(id) } returns false

            mockMvc.delete("/api/v1/books/$id")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }
}