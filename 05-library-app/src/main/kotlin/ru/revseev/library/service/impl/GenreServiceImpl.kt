package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.GenreRepo
import ru.revseev.library.service.GenreService

@Service
class GenreServiceImpl(private val genreRepo: GenreRepo) : GenreService {

    override fun getAll(): List<Genre> = wrapExceptions { genreRepo.findAll() }

}