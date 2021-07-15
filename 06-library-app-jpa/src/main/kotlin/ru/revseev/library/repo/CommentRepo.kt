package ru.revseev.library.repo

import ru.revseev.library.domain.Comment

interface CommentRepo {

    fun findById(id: Long): Comment?

    fun save(comment: Comment): Comment

    fun deleteById(id: Long): Boolean
}
