package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import ru.revseev.library.dao.GenreDao
import ru.revseev.library.domain.Genre
import ru.revseev.library.service.GenreService

@Service
class GenreServiceImpl(private val genreDao: GenreDao) : GenreService {

    override fun getAll(): List<Genre> = genreDao.getAll()

    override fun getById(id: Long): Genre = genreDao.getById(id)

    override fun getByName(name: String): Genre? = genreDao.getByName(name)

    override fun add(genre: Genre): Long = genreDao.add(genre)

    override fun update(genre: Genre): Boolean = genreDao.update(genre)

    override fun deleteById(id: Long): Boolean = genreDao.deleteById(id)
}