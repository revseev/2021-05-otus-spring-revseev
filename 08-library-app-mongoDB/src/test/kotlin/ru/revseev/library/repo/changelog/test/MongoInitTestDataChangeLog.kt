package ru.revseev.library.repo.changelog.test

import com.github.cloudyrock.mongock.ChangeLog
import com.github.cloudyrock.mongock.ChangeSet
import com.mongodb.client.MongoDatabase
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Comment
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.BookRepo

@ChangeLog(order = "001")
class MongoInitTestDataChangeLog {

    @ChangeSet(order = "000", id = "dropDB", author = "revseev", runAlways = true)
    fun dropDB(database: MongoDatabase) {
        database.drop()
    }

    @ChangeSet(order = "001", id = "insert books", author = "revseev", runAlways = true)
    fun populateBooks(bookRepo: BookRepo) {
        val book1 = Book("Book1",
            Author("Author1"),
            mutableListOf(Genre("Genre1"), Genre("Genre2"))
        ).apply {
            comments += Comment("Comment11")
        }
        val book2 = Book("Book2",
            Author("Author2"),
            mutableListOf(Genre("Genre2"))
        ).apply {
            comments += Comment("Comment21")
            comments += Comment("Comment22")
        }
        val book3 = Book("Book3",
            Author("Author1"),
            mutableListOf(Genre("Genre1"), Genre("Genre3"))
        )
        bookRepo.saveAll(listOf(book1, book2, book3))
    }

//    @ChangeSet(order = "002", id = "insert author", author = "revseev", runAlways = true)
//    fun populateAuthor(authorRepo: AuthorRepo) {
//        Author("Philip K. Dick")
//        Author("J. R. R. Tolkien")
//        Author("New")
//
//
//        authorRepo.saveAll(listOf(Author("Philip K. Dick"), Author("J. R. R. Tolkien"), Author("New")))
//    }
}