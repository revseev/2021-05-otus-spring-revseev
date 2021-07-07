package ru.revseev.library.repo.impl

import org.springframework.stereotype.Repository
import ru.revseev.library.domain.Book
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.repo.GenreRepo
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.PersistenceContext
import javax.persistence.TypedQuery

@Repository
class BookRepoJpa(
    @PersistenceContext
    private val em: EntityManager,
    private val genreRepo: GenreRepo
) : BookRepo {

    override fun findAll(): List<Book> {
        return em.createQuery("SELECT DISTINCT b FROM Book b left join fetch b.genres", Book::class.java)
            .useFetchGraph("book-genre-graph")
            .resultList
    }

    override fun findById(id: Long): Book? {
        val query = em.createQuery(
            "SELECT DISTINCT b FROM Book b left join fetch b.genres where b.id = :id",
            Book::class.java
        )
            .setParameter("id", id)
            .useFetchGraph("book-genre-graph")

        return try {
            query.singleResult
        } catch (e: NoResultException) {
            return null
        }
    }

    override fun save(book: Book): Book {
        book.genres = genreRepo.saveAll(book.genres).toMutableList()

        return if (book.isNew()) {
            em.persistAndGet(book)
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

    private fun <T> TypedQuery<T>.useFetchGraph(graphName: String): TypedQuery<T> {
        val entityGraph = em.getEntityGraph(graphName)
        return this.setHint("javax.persistence.fetchgraph", entityGraph)
    }
}