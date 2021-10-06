package ru.revseev.library.service

import kotlinx.coroutines.flow.Flow
import ru.revseev.library.domain.Genre


interface GenreService {

    suspend fun getAll(): Flow<Genre>
}