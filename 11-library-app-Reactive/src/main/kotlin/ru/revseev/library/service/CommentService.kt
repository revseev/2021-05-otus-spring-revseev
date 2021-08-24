package ru.revseev.library.service

import kotlinx.coroutines.flow.Flow
import ru.revseev.library.domain.Comment
import ru.revseev.library.view.dto.NewCommentDto
import ru.revseev.library.view.dto.UpdatedCommentDto

interface CommentService {

    suspend fun getByBookId(bookId: String): Flow<Comment>

    suspend fun getById(id: String): Comment

    suspend fun add(newCommentDto: NewCommentDto): Comment

    suspend fun update(updatedCommentDto: UpdatedCommentDto): Comment

    suspend fun deleteById(id: String): Boolean
}
