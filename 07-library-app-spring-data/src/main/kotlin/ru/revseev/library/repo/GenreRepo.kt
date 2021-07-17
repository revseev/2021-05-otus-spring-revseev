package ru.revseev.library.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.revseev.library.domain.Genre

interface GenreRepo : JpaRepository<Genre, Long>, GenreRepositoryCustom {

    fun findByName(name: String): Genre?
}
