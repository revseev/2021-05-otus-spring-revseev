package ru.revseev.library.shell

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import ru.revseev.library.domain.Book
import ru.revseev.library.domain.Comment
import ru.revseev.library.domain.Genre
import ru.revseev.library.service.BookService
import ru.revseev.library.service.CommentService
import ru.revseev.library.service.GenreService
import ru.revseev.library.shell.dto.*

@ShellComponent
class AdminLibraryShell(
    private val bookService: BookService,
    private val genreService: GenreService,
    private val bookViewer: BookViewer,
    private val genreViewer: GenreViewer,
    private val genreParser: GenreParser,
    private val commentService: CommentService,
    private val commentViewer: CommentViewer,
) {

    @ShellMethod(value = "List all books.", key = ["books", "book list"])
    fun getAllBooks(): String = bookService.getAll().view()

    @ShellMethod(value = "Get a book by ID.", key = ["book", "book get"])
    fun getBookById(@ShellOption id: String): String = bookService.getById(id).view()

    @ShellMethod(
        value = """Add a book. Type a 'title' 'author'. Optionally follow
        genres as a single quoted string separated by commas (i.e. 'novel,science fiction,action')""",
        key = ["ba", "book add"]
    )
    fun addBook(
        @ShellOption title: String,
        @ShellOption authorName: String,
        @ShellOption(defaultValue = "") genres: String,
    ): String {
        val added = bookService.add(NewBookDto(title, authorName, genres.parseGenres()))
        return """|A book has been added:
                  |${added.view()}""".trimMargin()
    }


    @ShellMethod(
        value = """Update a book's genre, provide it's ID and genres
        as a single quoted string separated by commas (i.e. 'novel,science fiction,action')""",
        key = ["bu", "book update"]
    )
    fun updateBook(@ShellOption id: String, @ShellOption genresString: String): String {
        val updated = bookService.update(UpdatedBookDto(id, genresString.parseGenres()))
        return """|Updated successfully:
                  |${updated.view()}
        """.trimMargin()
    }

    @ShellMethod(value = "Delete a book by ID.", key = ["bd", "book delete"])
    fun deleteBook(@ShellOption id: String): String = bookService.deleteById(id).reportSuccess()

    @ShellMethod(value = "Get all genres", key = ["genres"])
    fun listGenres(): String = genreService.getAll().view()

    @ShellMethod(value = "List comments for a book, providing it's ID..", key = ["bc", "book comments"])
    fun getCommentsByBookId(@ShellOption bookId: String): String = commentService.getByBookId(bookId).view()

    @ShellMethod(
        value = "Leave a comment for a book, provide it's ID and comment as a single quoted string.",
        key = ["bca", "book comment add"]
    )
    fun createComment(@ShellOption bookId: String, @ShellOption body: String): String =
        commentService.add(NewCommentDto(bookId, body)).view()

    @ShellMethod(
        value = "Update a comment, provide it's ID and a new body as a single quoted string.",
        key = ["cu", "comment update"]
    )
    fun updateComment(@ShellOption id: String, @ShellOption body: String): String =
        commentService.update(UpdatedCommentDto(id, body)).view()

    @ShellMethod(value = "Delete a comment, providing it's ID", key = ["cd", "comment delete"])
    fun deleteComment(@ShellOption id: String): String = commentService.deleteById(id).reportSuccess()


    private fun Book.view(): String = bookViewer.view(this)

    private fun Collection<Book>.view(): String = bookViewer.viewList(this)

    @JvmName("viewComments")
    private fun Collection<Comment>.view(): String = commentViewer.viewList(this)

    private fun Comment.view(): String = commentViewer.view(this)

    @JvmName("viewGenres")
    private fun Collection<Genre>.view(): String = genreViewer.viewList(this)

    private fun String.parseGenres(): MutableList<GenreDto> = genreParser.parseGenres(this)

    private fun Boolean.reportSuccess(): String {
        return if (this) {
            "Deleted successfully"
        } else {
            "Was not successful"
        }
    }
}
