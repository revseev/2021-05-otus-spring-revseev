package ru.revseev.library.repo

import org.springframework.data.mongodb.repository.MongoRepository
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import java.util.*

interface BookRepo : MongoRepository<Book, String>, BookRepoCustom {

    override fun findAll(): List<Book>

     override fun findById(id: String): Optional<Book>
}
