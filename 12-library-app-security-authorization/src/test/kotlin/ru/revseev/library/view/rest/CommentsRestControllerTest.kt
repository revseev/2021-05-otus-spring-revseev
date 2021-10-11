package ru.revseev.library.view.rest

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.revseev.library.book2
import ru.revseev.library.comment21
import ru.revseev.library.comment22
import ru.revseev.library.service.CommentService
import ru.revseev.library.service.impl.CommentServiceImpl
import ru.revseev.library.view.CommentDtoConverter
import ru.revseev.library.view.dto.CommentDto
import ru.revseev.library.view.impl.CommentDtoConverterImpl

@WebMvcTest(CommentsRestController::class)
@ExtendWith(MockKExtension::class)
@Import(CommentDtoConverterImpl::class, CommentServiceImpl::class)
@AutoConfigureMockMvc(addFilters = false)
internal class CommentsRestControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var commentService: CommentService
    @SpykBean
    lateinit var commentDtoConverter: CommentDtoConverter

    @BeforeEach
    fun init() {
        clearAllMocks()
    }


    @Test
    fun `getAllByBookId() should return a list of CommentsDto by bookId`() {
        val bookId = book2.id
        val pagedComments = mutableListOf(comment21, comment22)

        every {
            commentService.getByBookId(bookId)
        } returns pagedComments
        val expected = pagedComments.map { commentDtoConverter.toDto(it) }

        mockMvc.get("/api/v1/books/$bookId/comments")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$", Matchers.hasSize<CommentDto>(2))
                    jsonPath("$[0].id", Matchers.`is`(expected[0].id))
                    jsonPath("$[0].body", Matchers.`is`(expected[0].body))
                    jsonPath("$[1].id", Matchers.`is`(expected[1].id))
                    jsonPath("$[1].body", Matchers.`is`(expected[1].body))
                }
            }
    }
}
