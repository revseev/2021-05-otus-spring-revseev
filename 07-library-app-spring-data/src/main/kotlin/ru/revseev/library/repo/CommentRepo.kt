package ru.revseev.library.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.revseev.library.domain.Comment

interface CommentRepo : JpaRepository<Comment, Long> {

    fun findByBookId(bookId: Long): MutableList<Comment>

    fun deleteCommentById(id: Long): Boolean

//    fun findById(id: Long): Comment?

//    fun save(comment: Comment): Comment

//    fun deleteById(id: Long): Boolean
}
