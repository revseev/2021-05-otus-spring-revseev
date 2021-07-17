package ru.revseev.library.repo.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Repository
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.GenreRepo
import ru.revseev.library.repo.GenreRepositoryCustom
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class GenreRepositoryCustomImpl(@PersistenceContext private val em: EntityManager) : GenreRepositoryCustom {

    @Lazy
    @Autowired
    lateinit var genreRepo: GenreRepo

    override fun saveAll(genres: List<Genre>): List<Genre> {
        val (new, notNew) = genres.partition { it.isNew() }

        val persisted = if (new.isNotEmpty()) {
            val existingNameToGenre = em.createQuery("SELECT g FROM Genre g WHERE g.name IN :genres", Genre::class.java)
                .setParameter("genres", new.map { it.name })
                .resultList
                .associateBy { it.name }

            new.map {
                existingNameToGenre[it.name] ?: genreRepo.save(it)
            }
        } else {
            emptyList()
        }

        val merged = if (notNew.isNotEmpty()) {
            notNew.map { em.merge(it) }
        } else {
            emptyList()
        }
        em.flush()
        return persisted + merged
    }

    override fun save(genre: Genre): Genre {
        var existingByName: Genre? = null
        if (genre.isNew()) {
            existingByName = genreRepo.findByName(genre.name)
        }
        return existingByName ?: genreRepo.saveAndFlush(genre)
    }
}
