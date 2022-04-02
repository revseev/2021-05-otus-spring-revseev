package ru.revseev.library.view

import ru.revseev.library.domain.Comment
import ru.revseev.library.view.dto.CommentDto

interface CommentDtoConverter {

    fun toDto(comment: Comment): CommentDto
}