package ru.revseev.library.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.revseev.library.domain.Genre

interface GenreRepo : JpaRepository<Genre, Long>, GenreRepositoryCustom {

//    fun findAll(): List<Genre>

    fun findByName(name: String): Genre?

//    fun save(genre: Genre): Genre

    override fun saveAll(genres: List<Genre>): List<Genre>

//    fun findById(id: Long): Genre?
}
