package ru.revseev.library.service

import ru.revseev.library.domain.Genre


interface GenreService {

    fun getAll(): List<Genre>
}