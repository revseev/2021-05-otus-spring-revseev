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

    override fun findByName(name: String): Genre? {
        return em.createQuery("SELECT g FROM Genre g WHERE g.name = :name", Genre::class.java)
            .setParameter("name", name)
            .resultList
            .firstOrNull()
    }

    override fun save(genre: Genre): Genre {
        return if (genre.isNew()) {
            return findByName(genre.name) ?: em.persistAndGet(genre)
        } else {
            em.merge(genre)
        }
    }
}

fun <T> EntityManager.persistAndGet(entity: T): T {
    this.persist(entity)
    return entity;
}