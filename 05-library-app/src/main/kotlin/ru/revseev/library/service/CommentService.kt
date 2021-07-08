package ru.revseev.library.service

import ru.revseev.library.domain.Comment
import ru.revseev.library.shell.dto.NewCommentDto
import ru.revseev.library.shell.dto.UpdatedCommentDto

interface CommentService {

    fun getByBookId(bookId: Long): MutableList<Comment>

    fun getById(id: Long): Comment

    fun add(newCommentDto: NewCommentDto): Comment

    fun update(updatedCommentDto: UpdatedCommentDto): Comment

    fun deleteById(id: Long): Boolean
}
