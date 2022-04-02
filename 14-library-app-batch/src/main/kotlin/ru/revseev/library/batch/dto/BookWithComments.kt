package ru.revseev.library.batch.dto

import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Comment

class BookWithComments(val book: Book, val comments: List<Comment>) {

    override fun toString(): String {
        return "BookWithComments(book=$book, comments=$comments)"
    }
}