package ru.revseev.library.repo

import ru.revseev.library.domain.Genre

interface GenreRepo {

    fun findAll(): List<Genre>
}
