package ru.revseev.library.repo

import org.springframework.data.mongodb.repository.MongoRepository
import ru.revseev.library.domain.Genre

interface GenreRepo : MongoRepository<Genre, Long> /*,GenreRepositoryCustom*/ {

    fun findByName(name: String): Genre?
}
