package ru.revseev.library.dao.impl

import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.revseev.library.dao.BookDao
import ru.revseev.library.dao.nullableSingleResult
import ru.revseev.library.dao.wrapExceptions
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import java.sql.ResultSet

@Repository
class BookDaoImpl(private val jdbc: NamedParameterJdbcTemplate) : BookDao {

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

    override fun add(book: Book): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(book: Book): Boolean {
        TODO("Not yet implemented")
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
}