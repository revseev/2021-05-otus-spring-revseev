package ru.revseev.library.repo

import ru.revseev.library.domain.Comment

interface CommentRepo {

    fun findByBookId(bookId: Long): MutableList<Comment>

    fun findById(id: Long): Comment
}
