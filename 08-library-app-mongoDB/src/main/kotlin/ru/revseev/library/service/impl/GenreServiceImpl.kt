package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.domain.Genre
import ru.revseev.library.repo.BookRepo
import ru.revseev.library.service.GenreService

@Service
class GenreServiceImpl(private val bookRepo: BookRepo) : GenreService {

    @Transactional(readOnly = true)
    override fun getAll(): List<Genre> = bookRepo.findAllGenres()
}