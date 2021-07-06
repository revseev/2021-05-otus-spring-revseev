package ru.revseev.library.repo.impl

import org.springframework.stereotype.Repository
import ru.revseev.library.domain.Book
import ru.revseev.library.repo.BookRepo
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.PersistenceContext

@Repository
class BookRepoJpa(@PersistenceContext private val em: EntityManager) : BookRepo {

    override fun findAll(): List<Book> {
        return em.createQuery(
            "SELECT DISTINCT b FROM Book b join fetch b.genres join fetch b.author",
            Book::class.java
        ).resultList
    }

    override fun findById(id: Long): Book? {
        val query = em.createQuery(
            "SELECT DISTINCT b FROM Book b join fetch b.genres join fetch b.author where b.id = :id",
            Book::class.java
        ).setParameter("id", id)

        return try {
            query.singleResult
        } catch (e: NoResultException) {
            return null
        }
    }

    override fun save(book: Book): Book {
        return if (book.isNew()) {
            em.persist(book)
            book
        } else {
            em.merge(book)
        }
    }

    override fun deleteById(id: Long): Boolean {
        val changedRows: Int = em.createQuery("delete from Book b where b.id = :id")
            .setParameter("id", id)
            .executeUpdate()

        return if (changedRows > 0) {
            em.clear()
            true
        } else {
            false
        }
    }
}