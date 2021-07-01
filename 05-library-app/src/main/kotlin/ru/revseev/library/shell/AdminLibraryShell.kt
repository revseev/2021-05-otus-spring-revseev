package ru.revseev.library.shell

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import ru.revseev.library.service.BookService

@ShellComponent
class AdminLibraryShell(private val bookService: BookService, private val bookViewer: BookViewer) {

    @ShellMethod(value = "List all books.", key = ["books", "book list"])
    fun getAllBooks(): String = bookService.getAll().let {
        bookViewer.viewList(it)
    }

    @ShellMethod(
        value = """Add a book. Type a 'title' 'author'. 
Optionally follow genres as a single quoted string separated by commas (i.e. 'novel,science fiction,action')""",
        key = ["book add"]
    )
    fun addBook(
        @ShellOption title: String,
        author: String,
        @ShellOption(defaultValue = "") genres: String
    ): String {
        val genreNames = genres.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()

        return "1) $title 2) $author 3) ${genreNames.joinToString(", ")}"
    }
}
