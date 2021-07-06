package ru.revseev.library.shell

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import ru.revseev.library.domain.Author
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Genre
import ru.revseev.library.service.BookService
import ru.revseev.library.service.GenreService
import ru.revseev.library.shell.dto.GenreDto
import ru.revseev.library.shell.dto.toGenres

@ShellComponent
class AdminLibraryShell(
    private val bookService: BookService,
    private val genreService: GenreService,
    private val bookViewer: BookViewer,
    private val genreViewer: GenreViewer,
    private val genreParser: GenreParser,
) {

    @ShellMethod(value = "List all books.", key = ["books", "book list"])
    fun getAllBooks(): String = bookService.getAll().view()

    @ShellMethod(value = "Get a book by id.", key = ["book", "book get"])
    fun getBookById(@ShellOption id: Long): String = bookService.getById(id).view()

    @ShellMethod(
        value = """Add a book. Type a 'title' 'author'. Optionally follow
        genres as a single quoted string separated by commas (i.e. 'novel,science fiction,action')""",
        key = ["ba", "book add"]
    )
    fun addBook(
        @ShellOption title: String,
        @ShellOption author: String,
        @ShellOption(defaultValue = "") genres: String,
    ): String {
        val genreList = genres.parseGenres().toGenres()
        val newBook = Book(title, Author(author), genreList)
        val added = bookService.add(newBook)
        return """|A book has been added:
                  |${added.view()}""".trimMargin()
    }


    @ShellMethod(
        value = """Update a book's genre, provide it's id and genres 
        as a single quoted string separated by commas (i.e. 'novel,science fiction,action')""",
        key = ["bu", "book update"]
    )
    fun updateBook(@ShellOption id: Long, @ShellOption genresString: String): String {
        val book = bookService.getById(id)
            .apply {
                genres = genresString.parseGenres().toGenres()
            }

        val updated = bookService.update(book)
        return """|Updated successfully:
                  |${updated.view()}
        """.trimMargin()
    }

    @ShellMethod(value = "Delete a book by id.", key = ["bd", "book delete"])
    fun deleteBook(@ShellOption id: Long): String {
        val isDeleted = bookService.deleteById(id)

        return if (isDeleted) {
            "Deleted successfully"
        } else {
            "Was not successful"
        }
    }

    @ShellMethod(value = "Get all genres", key = ["genres"])
    fun listGenres(): String = genreService.getAll().view()

    private fun Book.view(): String = bookViewer.view(this)

    private fun Collection<Book>.view(): String = bookViewer.viewList(this)

    @JvmName("viewGenres")
    private fun Collection<Genre>.view(): String = genreViewer.viewList(this)

    private fun String.parseGenres(): MutableList<GenreDto> = genreParser.parseGenres(this)
}
