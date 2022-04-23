package ru.revseev.library.repo.changelog.test

import com.github.cloudyrock.mongock.ChangeLog
import com.github.cloudyrock.mongock.ChangeSet
import com.mongodb.client.MongoDatabase
import ru.revseev.library.*
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.repo.CommentRepo
import ru.revseev.library.repo.UserRepo

@ChangeLog(order = "001")
class MongoInitTestDataChangeLog {

    @ChangeSet(order = "001", id = "dropDB", author = "revseev", runAlways = true)
    fun dropDB(database: MongoDatabase) {
        database.drop()
    }

    @ChangeSet(order = "002", id = "insert books", author = "revseev", runAlways = true)
    fun populateData(bookRepo: BookRepo, commentRepo: CommentRepo) {
        commentRepo.saveAll(listOf(comment11, comment21, comment22))
        book1.commentIds += comment11.id
        book2.commentIds += comment21.id
        book2.commentIds += comment22.id
        bookRepo.saveAll(listOf(book1, book2, book3))
    }

    @ChangeSet(order = "003", id = "insert users", author = "revseev", runAlways = true)
    fun populateUsers(userRepo: UserRepo) {
        userRepo.save(admin)
        userRepo.save(user)
    }
}