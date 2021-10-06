package ru.revseev.library.repo.changelog.mongo

import com.github.cloudyrock.mongock.ChangeLog
import com.github.cloudyrock.mongock.ChangeSet
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate
import com.mongodb.client.MongoDatabase
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Comment
import ru.revseev.library.domain.Genre

@ChangeLog(order = "001")
class MongoInitDataChangeLog {

    @ChangeSet(order = "001", id = "dropDB", author = "revseev", runAlways = true)
    fun dropDB(database: MongoDatabase) {
        database.drop()
    }

    @ChangeSet(order = "002", id = "insert books", author = "revseev", runAlways = true)
    fun populateData(mongoTemplate: MongockTemplate) {
        val book1 = Book("Do Androids Dream of Electric Sheep?",
            Author("Philip K. Dick"),
            mutableListOf(Genre("Science Fiction"), Genre("Novel"))
        )
        val book2 = Book("The Hobbit",
            Author("J. R. R. Tolkien"),
            mutableListOf(Genre("Adventure"), Genre("Fiction"), Genre("Fantasy"))
        )
        val book3 = Book("The Little Prince",
            Author("Antoine de Saint-Exupéry"),
            mutableListOf(Genre("Fantasy"), Genre("Novel"))
        )
        val comment11 = Comment(book1.id, "I decided to read it after Blade Runner movie.")
        val comment21 = Comment(book2.id, "Greatest Adventure ever!")
        val comment22 = Comment(book2.id, "Loved it!")
        mongoTemplate.insertAll(listOf(comment11, comment21, comment22))

        book1.commentIds += comment11.id
        book2.commentIds += comment21.id
        book2.commentIds += comment22.id
        mongoTemplate.insertAll(listOf(book1, book2, book3))
    }
}