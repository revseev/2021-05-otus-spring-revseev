package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.GenreRepo
import ru.revseev.library.service.GenreService

@Service
class GenreServiceImpl(private val genreRepo: GenreRepo) : GenreService {

    @Transactional(readOnly = true)
    override fun getAll(): List<Genre> = wrapExceptions { genreRepo.findAll() }
}