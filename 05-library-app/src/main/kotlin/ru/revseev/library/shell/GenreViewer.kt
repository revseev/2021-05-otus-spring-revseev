package ru.revseev.library.shell

import ru.revseev.library.domain.Genre

interface GenreViewer {

    fun view(genre: Genre): String

    fun viewList(genres: Collection<Genre>): String
}