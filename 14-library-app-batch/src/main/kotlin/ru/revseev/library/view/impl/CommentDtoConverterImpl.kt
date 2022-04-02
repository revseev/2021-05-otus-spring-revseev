package ru.revseev.library.view.impl

import org.springframework.stereotype.Component
import ru.revseev.library.domain.Comment
import ru.revseev.library.view.CommentDtoConverter
import ru.revseev.library.view.dto.CommentDto

@Component
class CommentDtoConverterImpl : CommentDtoConverter {

    override fun toDto(comment: Comment): CommentDto = CommentDto(
        id = comment.id,
        bookId = comment.bookId,
        body = comment.body
    )
}