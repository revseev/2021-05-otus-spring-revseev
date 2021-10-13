package ru.revseev.library.repo.changelog.mongo

import com.github.cloudyrock.mongock.ChangeLog
import com.github.cloudyrock.mongock.ChangeSet
import com.mongodb.client.MongoDatabase
import ru.revseev.library.domain.*
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.repo.CommentRepo
import ru.revseev.library.repo.UserRepo

@ChangeLog(order = "001")
class MongoInitDataChangeLog {

    @ChangeSet(order = "001", id = "dropDB", author = "revseev", runAlways = true)
    fun dropDB(database: MongoDatabase) {
        database.drop()
    }

    @ChangeSet(order = "002", id = "insert books", author = "revseev", runAlways = true)
    fun populateData(bookRepo: BookRepo, commentRepo: CommentRepo) {
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
        commentRepo.saveAll(listOf(comment11, comment21, comment22))

        book1.commentIds += comment11.id
        book2.commentIds += comment21.id
        book2.commentIds += comment22.id
        bookRepo.saveAll(listOf(book1, book2, book3))
    }


    @ChangeSet(order = "003", id = "insert users", author = "revseev", runAlways = true)
    fun populateUsers(userRepo: UserRepo) {
        val admin = User("admin", "\$2a\$12\$X5nIrc2tqPnGk1Jqr7hwY.WPLdIQiXUIQtT5WRbptyW2N4jgdTMPy", mutableListOf(Role.ADMIN, Role.USER))
        userRepo.save(admin)
    }
}