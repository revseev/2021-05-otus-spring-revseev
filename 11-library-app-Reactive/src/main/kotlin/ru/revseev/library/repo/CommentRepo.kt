package ru.revseev.library.repo

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import ru.revseev.library.domain.Comment

interface CommentRepo : ReactiveMongoRepository<Comment, String> {

    override fun findAllById(ids: MutableIterable<String>): Flux<Comment>
}