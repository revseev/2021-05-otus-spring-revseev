package ru.revseev.library.service.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.domain.Comment
import ru.revseev.library.exception.LibraryItemNotFoundException
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.repo.CommentRepo
import ru.revseev.library.service.BookService
import ru.revseev.library.service.CommentService
import ru.revseev.library.view.dto.NewCommentDto
import ru.revseev.library.view.dto.UpdatedCommentDto

@Service
class CommentServiceImpl(
    private val commentRepo: CommentRepo,
    private val bookService: BookService,
    private val bookRepo: BookRepo,
) : CommentService {

    @Transactional(readOnly = true)
    override suspend fun getByBookId(bookId: String): Flow<Comment> {
        val commentIds = bookService.getById(bookId).commentIds
        return commentRepo.findAllById(commentIds).asFlow()
    }

    @Transactional(readOnly = true)
    override suspend fun getById(id: String): Comment = commentRepo.findById(id).awaitFirstOrElse {
        throw LibraryItemNotFoundException("Comment with id = $id was not found")
    }

    @Transactional
    override suspend fun add(newCommentDto: NewCommentDto): Comment {
        val book = bookService.getById(newCommentDto.bookId)
        val comment = commentRepo.save(Comment(book.id, newCommentDto.body)).awaitSingle()
        book.commentIds += comment.id
        bookRepo.save(book)
        return comment
    }

    @Transactional
    override suspend fun update(updatedCommentDto: UpdatedCommentDto): Comment {
        val comment = getById(updatedCommentDto.id).apply { body = updatedCommentDto.body }
        return commentRepo.save(comment).awaitSingle()
    }

    @Transactional
    override suspend fun deleteById(id: String): Boolean = try {
        commentRepo.deleteById(id).awaitSingle()
        true
    } catch (ex: Exception) {
        false
    }
}
