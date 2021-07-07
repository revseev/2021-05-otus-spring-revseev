package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import ru.revseev.library.domain.Comment
import ru.revseev.library.service.CommentService

@Service
class CommentServiceImpl : CommentService {

    override fun getByBookId(bookId: Long): MutableList<Comment> {
        TODO("Not yet implemented")
    }
}