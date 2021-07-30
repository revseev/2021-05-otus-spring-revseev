package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.domain.Comment
import ru.revseev.library.exception.LibraryItemNotFoundException
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.repo.CommentRepo
import ru.revseev.library.service.BookService
import ru.revseev.library.service.CommentService
import ru.revseev.library.shell.dto.NewCommentDto
import ru.revseev.library.shell.dto.UpdatedCommentDto

@Service
class CommentServiceImpl(
    private val commentRepo: CommentRepo,
    private val bookService: BookService,
    private val bookRepo: BookRepo,
) : CommentService {

    @Transactional(readOnly = true)
    override fun getByBookId(bookId: String): MutableList<Comment> {
        val commentIds = bookService.getById(bookId).commentIds
        return commentRepo.findAllById(commentIds)
    }

    @Transactional(readOnly = true)
    override fun getById(id: String): Comment = commentRepo.findById(id).orElseThrow {
        LibraryItemNotFoundException("Comment with id = $id was not found")
    }

    @Transactional
    override fun add(newCommentDto: NewCommentDto): Comment {
        val book = bookService.getById(newCommentDto.bookId)
        val comment = commentRepo.save(Comment(book.id, newCommentDto.body))
        book.commentIds += comment.id
        bookRepo.save(book)
        return comment
    }

    @Transactional
    override fun update(updatedCommentDto: UpdatedCommentDto): Comment {
        val comment = getById(updatedCommentDto.id).apply { body = updatedCommentDto.body }
        return commentRepo.save(comment)
    }

    @Transactional
    override fun deleteById(id: String): Boolean = try {
        commentRepo.deleteById(id)
        true
    } catch (ex: Exception) {
        false
    }
}
