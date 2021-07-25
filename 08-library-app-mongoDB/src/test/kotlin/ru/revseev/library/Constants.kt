package ru.revseev.library

import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Comment
import ru.revseev.library.domain.Genre

val genre1 = Genre("Genre1")
val genre2 = Genre("Genre2")
val genre3 = Genre("Genre3")
val author1 = Author("Author1")
val author2 = Author("Author2")
val comment11 = Comment("Comment11")
val comment21 = Comment("Comment21")
val comment22 = Comment("Comment22")
val book1 = Book("Book1", author1, mutableListOf(genre1, genre2)).apply { comments += comment11 }
val book2 = Book("Book2", author2, mutableListOf(genre2)).apply {
    comments += comment21
    comments += comment22
}
val book3 = Book("Book3", author1, mutableListOf(genre1, genre3))
