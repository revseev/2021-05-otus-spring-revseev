package ru.revseev.library.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import ru.revseev.library.domain.Book

interface BookRepo : MongoRepository<Book, String>, BookRepoCustom {

    override fun findAll(): List<Book>

    override fun findAll(pageable: Pageable): Page<Book>
}
