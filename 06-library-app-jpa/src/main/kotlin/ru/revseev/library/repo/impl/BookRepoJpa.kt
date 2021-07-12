package ru.revseev.library.repo.impl

import org.springframework.stereotype.Repository
import org.springframework.util.Assert
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Comment
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.repo.GenreRepo
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.TypedQuery

@Repository
class BookRepoJpa(
    @PersistenceContext
    private val em: EntityManager,
    private val genreRepo: GenreRepo,
) : BookRepo {

    override fun findAll(): List<Book> = em.createQuery(
        "SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.genres", Book::class.java
    )
        .useFetchGraph("book-author-graph")
        .resultList

    override fun findById(id: Long): Book? = em.createQuery(
        "SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.genres WHERE b.id = :id", Book::class.java
    )
        .setParameter("id", id)
        .useFetchGraph("book-author-graph")
        .resultList
        .firstOrNull()

    override fun save(book: Book): Book {
        book.genres = genreRepo.saveAll(book.genres).toMutableList()

        return if (book.isNew()) {
            em.persistAndGet(book)
        } else {
            em.merge(book)
        }
    }

    override fun deleteById(id: Long): Boolean {
        val changedRows: Int = em.createQuery("DELETE FROM Book WHERE id = :id")
            .setParameter("id", id)
            .executeUpdate()

        return if (changedRows > 0) {
            em.clear()
            true
        } else {
            false
        }
    }


    override fun getBookWithComments(book: Book): Book {
        Assert.notNull(book.id, "Book must have an id")
        // поскольку, в 99% случаев комменты в книге не нужны, лучше их вынуть подзапросом
        val comments = em.createQuery(
            "SELECT c FROM Comment c WHERE c.book.id = :bookId",
            Comment::class.java
        )
            .setParameter("bookId", book.id)
            .resultList
        book.comments = comments
        return book
    }

    private fun <T> TypedQuery<T>.useFetchGraph(graphName: String): TypedQuery<T> {
        val entityGraph = em.getEntityGraph(graphName)
        return this.setHint("javax.persistence.fetchgraph", entityGraph)
    }
}