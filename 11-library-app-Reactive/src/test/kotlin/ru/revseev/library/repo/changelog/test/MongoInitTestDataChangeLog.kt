package ru.revseev.library.repo.changelog.test

import com.github.cloudyrock.mongock.ChangeLog
import com.github.cloudyrock.mongock.ChangeSet
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate
import com.mongodb.client.MongoDatabase
import ru.revseev.library.*

@ChangeLog(order = "001")
class MongoInitTestDataChangeLog {

    @ChangeSet(order = "001", id = "dropDB", author = "revseev", runAlways = true)
    fun dropDB(database: MongoDatabase) {
        database.drop()
    }

    @ChangeSet(order = "002", id = "insert books", author = "revseev", runAlways = true)
    fun populateData(mongoTemplate: MongockTemplate) {
        mongoTemplate.insertAll(listOf(comment11, comment21, comment22))
        book1.commentIds += comment11.id
        book2.commentIds += comment21.id
        book2.commentIds += comment22.id
        mongoTemplate.insertAll(listOf(book1, book2, book3))
    }
}