package ru.revseev.library.repo

import org.springframework.data.mongodb.repository.MongoRepository
import ru.revseev.library.domain.Book
import java.util.*

interface BookRepo : MongoRepository<Book, Long> {

//    @EntityGraph(attributePaths = ["author", "genres"])
    override fun findAll(): List<Book>

//    @EntityGraph(attributePaths = ["author", "genres"])
    override fun findById(id: Long): Optional<Book>
}
