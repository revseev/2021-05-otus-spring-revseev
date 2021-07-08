package ru.revseev.library

import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Comment
import ru.revseev.library.domain.Genre

const val existingId1 = 1L
const val existingId2 = 2L
const val nonExistingId = 0L

val genre1 = Genre("Genre1").apply { id = existingId1 }
val genre2 = Genre("Genre2").apply { id = existingId2 }
val genre3 = Genre("Genre3").apply { id = 3L }
val author1 = Author("Author1").apply { id = existingId1 }
val author2 = Author("Author2").apply { id = existingId2 }
val book1 = Book("Book1", author1, mutableListOf(genre1, genre2)).apply { id = existingId1 }
val book2 = Book("Book2", author2, mutableListOf(genre2)).apply { id = existingId2 }
val comment11 = Comment(book1, "Comment11").apply { id = existingId1 }
val comment12 = Comment(book1, "Comment12").apply { id = existingId2 }