package ru.revseev.library.repo.impl

import org.springframework.stereotype.Repository
import ru.revseev.library.domain.Comment
import ru.revseev.library.repo.CommentRepo
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class CommentRepoJpa(@PersistenceContext private val em: EntityManager) : CommentRepo {

    override fun findById(id: Long): Comment? = em.createQuery(
        "SELECT c FROM Comment c WHERE c.id = :id",
        Comment::class.java
    )
        .setParameter("id", id)
        .resultList
        .firstOrNull()

    override fun save(comment: Comment): Comment {
        return if (comment.isNew()) {
            em.persistAndGet(comment)
        } else {
            em.merge(comment)
        }
    }

    override fun deleteById(id: Long): Boolean {
        val changed: Int = em.createQuery("DELETE FROM Comment WHERE id = :id")
            .setParameter("id", id)
            .executeUpdate()
        return changed > 0
    }
}