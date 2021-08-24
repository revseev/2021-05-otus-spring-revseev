package ru.revseev.library.repo.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.BookRepoCustom

class BookRepoCustomImpl(private val mongo: ReactiveMongoTemplate) : BookRepoCustom {

    override suspend fun findAllGenres(): Flow<Genre> = mongo.aggregate(
        newAggregation(
            project().andExclude("_id").and("genres.name").`as`("name"),
            unwind("name"),
            group("name"),
            project().and("_id").`as`("name").andExclude("_id"),
            sort(Sort.Direction.ASC, "name")
        ), Book::class.java, Genre::class.java
    ).asFlow()
}

