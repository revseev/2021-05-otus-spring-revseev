package ru.revseev.library.service

import ru.revseev.library.domain.Comment
import ru.revseev.library.view.dto.NewCommentDto
import ru.revseev.library.view.dto.UpdatedCommentDto

interface CommentService {

    fun getByBookId(bookId: String): MutableList<Comment>

    fun getById(id: String): Comment

    fun add(newCommentDto: NewCommentDto): Comment

    fun update(updatedCommentDto: UpdatedCommentDto): Comment

    fun deleteById(id: String): Boolean
}
