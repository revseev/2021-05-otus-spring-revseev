package ru.revseev.library.shell.impl

import org.springframework.stereotype.Service
import ru.revseev.library.domain.Genre
import ru.revseev.library.shell.GenreViewer

@Service
class GenreViewerImpl : GenreViewer {

    override fun view(genre: Genre): String = "[${genre.id}] ${genre.name}"

    override fun viewList(genres: Collection<Genre>): String = genres.joinToString(System.lineSeparator()) { view(it) }
}