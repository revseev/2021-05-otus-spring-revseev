package ru.revseev.library.repo.impl

import org.springframework.stereotype.Repository
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.GenreRepo
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class GenreRepoJpa(@PersistenceContext private val em: EntityManager) : GenreRepo {

    override fun findAll(): List<Genre> {
        return em.createQuery("select g from Genre g", Genre::class.java).resultList
    }
}