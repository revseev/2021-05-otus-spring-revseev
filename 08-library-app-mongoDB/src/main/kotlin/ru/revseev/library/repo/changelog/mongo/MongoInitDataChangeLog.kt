package ru.revseev.library.repo.changelog.mongo

import com.github.cloudyrock.mongock.ChangeLog
import com.github.cloudyrock.mongock.ChangeSet
import com.mongodb.client.MongoDatabase
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Comment
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.AuthorRepo
import ru.revseev.library.repo.BookRepo

@ChangeLog(order = "001")
class MongoInitDataChangeLog {

    @ChangeSet(order = "000", id = "dropDB", author = "revseev", runAlways = true)
    fun dropDB(database: MongoDatabase) {
        database.drop()
    }

    @ChangeSet(order = "001", id = "insert books", author = "revseev", runAlways = true)
    fun populateBooks(bookRepo: BookRepo) {
        val book1 = Book("Do Androids Dream of Electric Sheep?",
            Author("Philip K. Dick"),
            mutableListOf(Genre("Science Fiction"), Genre("Novel"))
        ).apply {
            comments += Comment("I decided to read it after Blade Runner movie.")
        }
        val book2 = Book("The Hobbit",
            Author("J. R. R. Tolkien"),
            mutableListOf(Genre("Adventure"), Genre("Fiction"), Genre("Fantasy"))
        ).apply {
            comments += Comment("Greatest Adventure ever!")
            comments += Comment("Loved it!")
        }
        val book3 = Book("The Little Prince",
            Author("Antoine de Saint-Exupéry"),
            mutableListOf(Genre("Fantasy"), Genre("Novel"))
        )


        bookRepo.saveAll(listOf(book1, book2, book3))
    }

    @ChangeSet(order = "002", id = "insert author", author = "revseev", runAlways = true)
    fun populateAuthor(authorRepo: AuthorRepo) {
        Author("Philip K. Dick")
        Author("J. R. R. Tolkien")
        Author("New")


        authorRepo.saveAll(listOf(Author("Philip K. Dick"), Author("J. R. R. Tolkien"), Author("New")))
    }
}