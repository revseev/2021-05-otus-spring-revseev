package ru.revseev.library.shell.impl

import org.springframework.stereotype.Service
import ru.revseev.library.domain.Book
import ru.revseev.library.shell.BookViewer

@Service
class BookViewerImpl : BookViewer {

    override fun view(book: Book): String {
        return """
            |[${book.id}] "${book.title}" 
            |    Author: ${book.author.name}
            |    Genres: ${book.genres.joinToString(separator = ", ") { it.name }}
        """.trimMargin()
    }

    override fun viewList(books: Collection<Book>): String {
        return books.sortedBy { it.id }.joinToString(System.lineSeparator()) {
            view(it)
        }
    }
}