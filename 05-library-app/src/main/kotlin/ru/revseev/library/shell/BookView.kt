package ru.revseev.library.shell

import ru.revseev.library.domain.Book

interface BookViewer {
    
    fun view(book: Book): String

    fun viewList(books: Collection<Book>): String
}