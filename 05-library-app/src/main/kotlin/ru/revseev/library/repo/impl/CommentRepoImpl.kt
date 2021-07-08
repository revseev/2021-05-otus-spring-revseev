package ru.revseev.library.repo.impl

import org.springframework.stereotype.Repository
import ru.revseev.library.domain.Comment
import ru.revseev.library.repo.CommentRepo
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class CommentRepoImpl(@PersistenceContext private val em: EntityManager) : CommentRepo {

    override fun findByBookId(bookId: Long): MutableList<Comment> {
        return em.createQuery(
            "SELECT c FROM Comment c join fetch Author a ON a.id = c.book.author.id WHERE c.book.id = :bookId",
            Comment::class.java
        )
            .setParameter("bookId", bookId)
            .resultList
            .toMutableList()
    }

    override fun findById(id: Long): Comment {
        return em.createQuery(
            "SELECT c FROM Comment c JOIN FETCH Author a ON a.id = c.book.author.id WHERE c.id = :id",
            Comment::class.java
        )
            .setParameter("id", id)
            .singleResult
    }


}