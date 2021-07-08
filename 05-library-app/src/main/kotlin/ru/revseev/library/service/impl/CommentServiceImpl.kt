package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.domain.Comment
import ru.revseev.library.repo.CommentRepo
import ru.revseev.library.service.CommentService

@Service
class CommentServiceImpl(private val commentRepo: CommentRepo) : CommentService {

    @Transactional
    override fun getByBookId(bookId: Long): MutableList<Comment> = wrapExceptions { commentRepo.findByBookId(bookId) }

    @Transactional
    override fun getById(id: Long): Comment = wrapExceptions { commentRepo.findById(id) }
}