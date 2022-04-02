package ru.revseev.library.batch

import mu.KotlinLogging
import org.springframework.batch.item.ItemWriter
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.batch.dto.BookWithComments
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Comment
import ru.revseev.library.domain.Genre

private val log = KotlinLogging.logger { }

@Component
class PostgresBookWriter(
    private val jdbc: NamedParameterJdbcTemplate,
) : ItemWriter<BookWithComments> {

    override fun write(items: MutableList<out BookWithComments>) {
        items.forEach(::write)
    }

    @Transactional
    fun write(bookWithComments: BookWithComments) {
        val book = bookWithComments.book
        val authorId = writeAuthor(book.author)
        log.debug { "Done: Author's id = $authorId" }
        val genreIds = writeGenres(book.genres)
        log.debug { "Done: Genres id's = $genreIds" }
        val bookId = writeBook(book.title, authorId)
        log.debug { "Done: Book's id = $bookId" }
        val writeBookToGenreRelations = writeBookToGenreRelations(bookId, genreIds)
        log.debug { "Done: written book_genre relations = $writeBookToGenreRelations" }
        val writeComments = writeComments(bookId, bookWithComments.comments)
        log.debug { "Done: written book_genre relations = $writeComments" }
    }

    fun writeAuthor(author: Author): Long {
        val name = author.name
        log.debug { "Writing Author: $author" }
        val params = MapSqlParameterSource("name", name)

        val existingId: Long? = wrapExceptionsNullable("Error fetching Author with name = $name") {
            jdbc.query("SELECT id FROM authors WHERE name = :name", params) { rs, _ ->
                rs.getLong("id")
            }.firstOrNull()
        }
        return existingId ?: wrapExceptions("Error adding Author: {name = $name}") {
            val keyHolder = GeneratedKeyHolder()
            jdbc.update("INSERT INTO authors(name) VALUES (:name)", params, keyHolder)
            keyHolder.keys!!["id"] as Long
        }
    }

    fun writeGenres(genres: List<Genre>): List<Long> = genres.map(::writeGenre)

    fun writeGenre(genre: Genre): Long {
        log.debug { "Writing Genre: $genre" }
        val name = genre.name
        val params = MapSqlParameterSource("name", name)

        val existingId: Long? = wrapExceptionsNullable("Error fetching Genre with name = $name") {
            jdbc.query("SELECT id FROM genres WHERE name = :name", params) { rs, _ ->
                rs.getLong("id")
            }.firstOrNull()
        }
        return existingId ?: wrapExceptions("Error adding Genre: {name = $name}") {
            val keyHolder = GeneratedKeyHolder()
            jdbc.update("INSERT INTO genres(name) VALUES (:name)", params, keyHolder)
            keyHolder.keys!!["id"] as Long
        }
    }

    fun writeBook(title: String, authorId: Long): Long {
        val infoTemplate = "Book: {title = $title, authorId = $authorId}"
        log.debug { "Writing $infoTemplate" }
        val params = MapSqlParameterSource("title", title)
            .addValue("authorId", authorId)

        return wrapExceptions("Error adding $infoTemplate") {
            val keyHolder = GeneratedKeyHolder()
            jdbc.update("INSERT INTO books(title,author_id) VALUES (:title, :authorId)", params, keyHolder)
            keyHolder.keys!!["id"] as Long
        }
    }

    fun writeBookToGenreRelations(bookId: Long, genreIds: List<Long>): Int {
        val infoTemplate = "book_genre relations: {bookId = $bookId, genreIds = $genreIds}"
        log.debug { "Writing $infoTemplate" }

        val batchParams = genreIds.map { mapOf("bookId" to bookId, "authorId" to it) }.toTypedArray()
        return wrapExceptions("Error adding $infoTemplate") {
            jdbc.batchUpdate(
                "INSERT INTO book_genres(book_id, genre_id) VALUES (:bookId, :authorId)",
                batchParams
            ).size
        }
    }

    fun writeComments(bookId: Long, comments: List<Comment>): Int {
        val infoTemplate = "Comments: {bookId = $bookId, comments = $comments}"
        log.debug { "Writing $infoTemplate" }

        val batchParams = comments.map { mapOf("bookId" to bookId, "body" to it.body) }.toTypedArray()
        return wrapExceptions("Error adding $infoTemplate") {
            jdbc.batchUpdate(
                "INSERT INTO comments(book_id, body) VALUES (:bookId, :body)",
                batchParams
            ).size
        }
    }
}

inline fun <reified T> wrapExceptions(errorMessage: String, action: () -> T?): T {
    return try {
        action.invoke()!!
    } catch (ex: Exception) {
        throw BatchWriterException(errorMessage, ex)
    }
}

inline fun <reified T> wrapExceptionsNullable(errorMessage: String, action: () -> T?): T? {
    return try {
        action.invoke()
    } catch (ex: Exception) {
        throw BatchWriterException(errorMessage, ex)
    }
}

class BatchWriterException(errorMessage: String, cause: Exception) : RuntimeException(errorMessage, cause)