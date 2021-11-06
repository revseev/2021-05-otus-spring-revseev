package ru.revseev.library.view.rest

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.revseev.library.service.CommentService
import ru.revseev.library.view.CommentDtoConverter
import ru.revseev.library.view.dto.CommentDto

private val log = KotlinLogging.logger { }

@RestController
class CommentsRestController(
    private val commentService: CommentService,
    private val commentDtoConverter: CommentDtoConverter,
) {

    @GetMapping("/api/v1/books/{bookId}/comments")
    fun getAllByBookId(@PathVariable bookId: String): List<CommentDto> {
        log.info { "GET: /api/v1/books/$bookId/comments" }
        return commentService.getByBookId(bookId).map { commentDtoConverter.toDto(it) }
    }
}
