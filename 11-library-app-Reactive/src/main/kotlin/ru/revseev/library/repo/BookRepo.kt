package ru.revseev.library.repo

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import ru.revseev.library.domain.Book

interface BookRepo : ReactiveMongoRepository<Book, String>, BookRepoCustom {

    override fun findAll(): Flux<Book>

    override fun findAll(sort: Sort): Flux<Book>
}
