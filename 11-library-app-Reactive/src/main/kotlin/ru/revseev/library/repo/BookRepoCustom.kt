package ru.revseev.library.repo

import reactor.core.publisher.Flux
import ru.revseev.library.domain.Genre

interface BookRepoCustom {

    suspend fun findAllGenres(): Flux<Genre>
}
