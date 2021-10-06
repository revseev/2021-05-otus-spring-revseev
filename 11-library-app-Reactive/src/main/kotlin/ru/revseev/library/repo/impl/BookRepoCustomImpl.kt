package ru.revseev.library.repo.impl

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import reactor.core.publisher.Flux
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.BookRepoCustom

class BookRepoCustomImpl(private val mongo: ReactiveMongoTemplate) : BookRepoCustom {

    override suspend fun findAllGenres(): Flux<Genre> = mongo.aggregate(
        newAggregation(
            project().andExclude("_id").and("genres.name").`as`("name"),
            unwind("name"),
            group("name"),
            project().and("_id").`as`("name").andExclude("_id"),
            sort(Sort.Direction.ASC, "name")
        ), Book::class.java, Genre::class.java
    )
}

