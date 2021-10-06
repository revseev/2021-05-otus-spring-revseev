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
val book1 = Book("Book1", author1, mutableListOf(genre1, genre2))
val book2 = Book("Book2", author2, mutableListOf(genre2))
val book3 = Book("Book3", author1, mutableListOf(genre1, genre3))
val comment11 = Comment(book1.id, "Comment11")
val comment21 = Comment(book2.id, "Comment21")
val comment22 = Comment(book2.id, "Comment22")

val init = {
    book1.commentIds += comment11.id
    book2.commentIds += comment21.id
    book2.commentIds += comment22.id
}

