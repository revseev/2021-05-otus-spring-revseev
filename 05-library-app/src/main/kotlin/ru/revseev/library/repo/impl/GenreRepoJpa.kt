package ru.revseev.library.repo.impl

import org.springframework.stereotype.Repository
import ru.revseev.library.domain.Genre
import ru.revseev.library.domain.LongIdentifiable
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

    override fun saveAll(genres: List<Genre>): List<Genre> {
        val (new, notNew) = genres.splitByNew()

        val persisted = if (new.isNotEmpty()) {
            val existingNameToGenre = em.createQuery("SELECT g FROM Genre g where g.name in :genres", Genre::class.java)
                .setParameter("genres", new.map { it.name })
                .resultList
                .associateBy { it.name }

            new.map {
                existingNameToGenre[it.name] ?: save(it)
            }
        } else {
            listOf()
        }

        val merged = if (notNew.isNotEmpty()) {
            notNew.map { em.merge(it) }
        } else {
            emptyList()
        }

        return persisted + merged
    }

}

fun <T : LongIdentifiable> List<T>.splitByNew(): Pair<List<T>, List<T>> {
    val byNew = this.groupBy { it.isNew() }
    return byNew.getOrDefault(true, listOf()) to byNew.getOrDefault(false, listOf())
}

fun <T> EntityManager.persistAndGet(entity: T): T {
    this.persist(entity)
    return entity
}