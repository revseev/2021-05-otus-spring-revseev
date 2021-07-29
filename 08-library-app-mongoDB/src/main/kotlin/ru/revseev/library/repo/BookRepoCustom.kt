package ru.revseev.library.repo

import ru.revseev.library.domain.Genre

interface BookRepoCustom  {

     fun findAllGenres(): List<Genre>

}
