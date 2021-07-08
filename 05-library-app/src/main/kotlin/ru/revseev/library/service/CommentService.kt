package ru.revseev.library.service

import ru.revseev.library.domain.Comment

interface CommentService {

    fun getByBookId(bookId: Long): MutableList<Comment>

    fun getById(id: Long): Comment
}
