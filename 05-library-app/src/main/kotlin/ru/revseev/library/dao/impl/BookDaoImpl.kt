package ru.revseev.library.dao.impl

import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.revseev.library.dao.BookDao
import ru.revseev.library.dao.wrapExceptions
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre

@Repository
class BookDaoImpl(private val jdbc: NamedParameterJdbcTemplate) : BookDao {

    override fun getAll(): List<Book> {
        val sql = """
           SELECT b.id as book_id, 
                  b.title as title,
                  a.id as author_id,
                  a.name as author_name,
                  g.id as genre_id,
                  g.name as genre_name
            FROM books b 
                JOIN authors a ON b.author_id = a.id
                JOIN book_genres bg ON bg.book_id = b.id
                JOIN genres g ON g.id = bg.genre_id
           """

        return wrapExceptions("Error getting all Books") {
            jdbc.query(sql, ResultSetExtractor<List<Book>> {
                val bookDataMap = mutableMapOf<Long, Pair<String, Author>>()
                val bookGenreMap = mutableMapOf<Long, MutableList<Genre>>()
                val authors = mutableMapOf<Long, Author>()
                val genres = mutableMapOf<Long, Genre>()

                while (it.next()) {
                    val bookId = it.getLong("book_id")
                    val title = it.getString("title")
                    val authorId = it.getLong("author_id")
                    val authorName = it.getString("author_name")
                    val genreId = it.getLong("genre_id")
                    val genreName = it.getString("genre_name")

                    val author = authors.computeIfAbsent(authorId) { Author(authorId, authorName) }
                    val genre = genres.computeIfAbsent(genreId) { Genre(genreId, genreName) }
                    val bookGenres = bookGenreMap.computeIfAbsent(bookId) { mutableListOf() }
                    bookGenres.add(genre)
                    bookDataMap.putIfAbsent(bookId, Pair(title, author))
                }
                val books = mutableListOf<Book>()
                for ((bookId, bookData) in bookDataMap) {
                    books.add(Book(bookId, bookData.first, bookData.second, bookGenreMap[bookId]!!.toList()))
                }
                return@ResultSetExtractor books
            })
        }
        /*
                        rs: ResultSet ->
                    val bookDataMap = mutableMapOf<Long, Pair<String, Author>>()
                    val bookGenreMap = mutableMapOf<Long, MutableList<Genre>>()
                    val authors = mutableMapOf<Long, Author>()
                    val genres = mutableMapOf<Long, Genre>()

                    while (rs.next()) {
                        val bookId = rs.getLong("book_id")
                        val title = rs.getString("title")
                        val authorId = rs.getLong("author_id")
                        val authorName = rs.getString("author_name")
                        val genreId = rs.getLong("genre_id")
                        val genreName = rs.getString("genre_name")

                        val author = authors.computeIfAbsent(authorId) { Author(authorId, authorName) }
                        val genre = genres.computeIfAbsent(genreId) { Genre(genreId, genreName) }
                        val bookGenres = bookGenreMap.computeIfAbsent(bookId) { mutableListOf() }
                        bookGenres.add(genre)
                        bookDataMap.putIfAbsent(bookId, Pair(title, author))
                    }
                    val books = mutableListOf<Book>()
                    for ((bookId, bookData) in bookDataMap) {
                        books.add(Book(bookId, bookData.first, bookData.second, bookGenreMap[bookId]!!.toList()))
                    }
                    return books
                }*/
    }

    override fun getById(id: Long): Book {
        TODO("Not yet implemented")
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
}