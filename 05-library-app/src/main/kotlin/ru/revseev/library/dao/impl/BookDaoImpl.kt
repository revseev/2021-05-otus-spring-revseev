package ru.revseev.library.dao.impl

import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.revseev.library.dao.*
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import java.sql.ResultSet

@Repository
class BookDaoImpl(
    private val jdbc: NamedParameterJdbcTemplate,
    private val authorDao: AuthorDao,
    private val genreDao: GenreDao
) : BookDao {

    override fun getAll(): List<Book> {
        val sql = """
        SELECT b.id    as book_id,
               b.title as book_title,
               a.id    as author_id,
               a.name  as author_name,
               g.id    as genre_id,
               g.name  as genre_name
        FROM books b
                 JOIN authors a ON b.author_id = a.id
                 JOIN book_genres bg ON bg.book_id = b.id
                 JOIN genres g ON g.id = bg.genre_id
        """.trimIndent()

        return wrapExceptions("Error getting all Books") {
            jdbc.query(sql, bookListExtractor())
        }
    }

    override fun getById(id: Long): Book {
        val params = MapSqlParameterSource("id", id)
        val sql = """
            SELECT b.id        as book_id,
                   b.title     as book_title,
                   b.author_id as author_id,
                   a.name      as author_name,
                   g.id        as genre_id,
                   g.name      as genre_name
            FROM books b
                     JOIN authors a ON b.author_id = a.id
                     JOIN book_genres bg ON b.id = bg.book_id
                     JOIN genres g on g.id = bg.genre_id
            WHERE b.id = :id
        """.trimIndent()

        return wrapExceptions("Error getting Book with id = $id") {
            jdbc.query(sql, params, bookListExtractor()).nullableSingleResult()
        }
    }

    override fun add(book: Book): Long {
        val bookId = book.id
        if (bookId != null) {
            return bookId
        }
        val title = book.title
        val authorId = authorDao.add(book.author)
        val genreIds: List<Long> = book.genres.map { genreDao.add(it) }

        val params = MapSqlParameterSource("title", title)
            .addValue("author_id", authorId)
        val keyHolder = GeneratedKeyHolder()
        val bookSql = "INSERT INTO books(title, author_id) VALUES (:title, :author_id)"

        return wrapExceptions("Error adding Book: {id = $bookId, title = $title, authorId = $authorId}") {
            jdbc.update(bookSql, params, keyHolder)
            val newBookId = keyHolder.key as Long
            addBookGenresRelation(newBookId, genreIds)
            return newBookId
        }
    }

    /** The only mutable state of a Book is it's Genres */
    override fun update(book: Book): Boolean {
        val bookId = book.id ?: return false

        val genreIds: List<Long> = book.genres.map { genreDao.add(it) }

        val deleteSql = "DELETE FROM book_genres where book_id = :bookId"
        return wrapExceptions("Error updating Book: {id = $bookId, title = ${book.title}, authorId = ${book.author.id}}") {
            jdbc.update(deleteSql, MapSqlParameterSource("bookId", bookId))
            addBookGenresRelation(bookId, genreIds)
            true
        }
    }

    override fun deleteById(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    private fun bookListExtractor(): ResultSetExtractor<List<Book>> {
        return ResultSetExtractor { rs: ResultSet ->
            val bookMap = mutableMapOf<Long, Book>()
            val genreMap = mutableMapOf<Long, Genre>()

            while (rs.next()) {
                val genreId = rs.getLong("genre_id")
                val genre = genreMap.computeIfAbsent(genreId) {
                    Genre(genreId, rs.getString("genre_name"))
                }

                val bookId = rs.getLong("book_id")
                val book = bookMap.computeIfAbsent(bookId) {
                    val title = rs.getString("book_title")
                    val authorId = rs.getLong("author_id")
                    val authorName = rs.getString("author_name")

                    Book(bookId, title, Author(authorId, authorName), mutableListOf())
                }
                book.genres.add(genre)
            }
            return@ResultSetExtractor bookMap.values.toList()
        }
    }

    private fun addBookGenresRelation(bookId: Long, genreIds: Collection<Long>): Int {
        val sql = "INSERT INTO book_genres (book_id, genre_id) VALUES (:bookId, :genreId)"
        val params = mutableMapOf("bookId" to bookId)

        var changed = 0
        for (genreId in genreIds) {
            params["genreId"] = genreId
            changed += jdbc.update(sql, params)
        }
        return changed
    }
}