package ru.revseev.library.repo

import ru.revseev.library.domain.Genre

interface GenreRepositoryCustom {

    fun saveAll(genres: List<Genre>): List<Genre>
}
