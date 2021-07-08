package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.domain.Comment
import ru.revseev.library.exception.LibraryItemNotFoundException
import ru.revseev.library.repo.CommentRepo
import ru.revseev.library.service.BookService
import ru.revseev.library.service.CommentService
import ru.revseev.library.shell.dto.NewCommentDto
import ru.revseev.library.shell.dto.UpdatedCommentDto

@Service
class CommentServiceImpl(private val commentRepo: CommentRepo, private val bookService: BookService) : CommentService {

    @Transactional
    override fun getByBookId(bookId: Long): MutableList<Comment> = wrapExceptions { commentRepo.findByBookId(bookId) }

    @Transactional
    override fun getById(id: Long): Comment = wrapExceptions {
        commentRepo.findById(id) ?: throw LibraryItemNotFoundException("Comment with id = $id was not found")
    }

    @Transactional
    override fun add(newCommentDto: NewCommentDto): Comment {
        val book = bookService.getById(newCommentDto.bookId)
        return wrapExceptions {
            commentRepo.save(Comment(book, newCommentDto.body))
        }
    }

    @Transactional
    override fun update(updatedCommentDto: UpdatedCommentDto): Comment {
        return wrapExceptions {
            val comment = getById(updatedCommentDto.id).apply { body = updatedCommentDto.body }
            commentRepo.save(comment)
        }
    }

    @Transactional
    override fun deleteById(id: Long): Boolean = wrapExceptions { commentRepo.deleteById(id) }
}
