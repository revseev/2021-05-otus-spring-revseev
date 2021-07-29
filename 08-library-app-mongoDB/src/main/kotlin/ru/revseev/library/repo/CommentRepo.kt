package ru.revseev.library.repo

import org.springframework.data.mongodb.repository.MongoRepository
import ru.revseev.library.domain.Comment

interface CommentRepo : MongoRepository<Comment, String> {

    override fun findAllById(ids: MutableIterable<String>): MutableList<Comment>

}