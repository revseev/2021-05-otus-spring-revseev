package ru.revseev.library.repo

import kotlinx.coroutines.flow.Flow
import ru.revseev.library.domain.Genre

interface BookRepoCustom {

    suspend fun findAllGenres(): Flow<Genre>
}
