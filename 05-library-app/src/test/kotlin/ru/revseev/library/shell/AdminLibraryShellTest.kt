package ru.revseev.library.shell

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.shell.Shell
import ru.revseev.library.service.BookService

@SpringBootTest
internal class AdminLibraryShellTest {

    @Autowired
    lateinit var shell: Shell

    @SpykBean
    lateinit var bookService: BookService

    @MockkBean
    lateinit var bookViewer: BookViewer

    @Test
    fun `given shell command 'books' should getAllBooks via BookService`() {
        shell.evaluate { "books" }

        verify(exactly = 1) { bookService.getAll() }
    }

    @Test
    fun `should view books via BookViewer`() {
        val books = bookService.getAll()
        shell.evaluate { "books" }

        verify(exactly = 1) { bookViewer.viewList(books) }
    }
}