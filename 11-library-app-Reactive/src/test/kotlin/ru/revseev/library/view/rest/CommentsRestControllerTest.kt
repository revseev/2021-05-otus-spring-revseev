package ru.revseev.library.view.rest

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.asFlow
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import org.springframework.test.web.reactive.server.WebTestClient
import ru.revseev.library.book2
import ru.revseev.library.comment21
import ru.revseev.library.comment22
import ru.revseev.library.service.CommentService
import ru.revseev.library.service.impl.CommentServiceImpl
import ru.revseev.library.view.CommentDtoConverter
import ru.revseev.library.view.dto.CommentDto
import ru.revseev.library.view.impl.CommentDtoConverterImpl

@WebFluxTest(CommentsRestController::class)
@ExtendWith(MockKExtension::class)
@Import(CommentDtoConverterImpl::class, CommentServiceImpl::class)
internal class CommentsRestControllerTest {

    @Autowired
    lateinit var webClient: WebTestClient

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

        coEvery {
            commentService.getByBookId(bookId)
        } returns pagedComments.asFlow()
        val expected = pagedComments.map { commentDtoConverter.toDto(it) }

        webClient.get().uri("/api/v1/books/$bookId/comments")
            .exchange()

            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .consumeWith {
                println(it)
                jsonPath("$", Matchers.hasSize<CommentDto>(2))
                jsonPath("$[0].id", Matchers.`is`(expected[0].id))
                jsonPath("$[0].body", Matchers.`is`(expected[0].body))
                jsonPath("$[1].id", Matchers.`is`(expected[1].id))
                jsonPath("$[1].body", Matchers.`is`(expected[1].body))
            }
    }
}
