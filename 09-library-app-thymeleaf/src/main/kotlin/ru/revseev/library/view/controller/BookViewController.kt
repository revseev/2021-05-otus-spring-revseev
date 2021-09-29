package ru.revseev.library.view.controller

import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.RedirectView
import ru.revseev.library.service.BookService
import ru.revseev.library.service.CommentService
import ru.revseev.library.service.GenreService
import ru.revseev.library.view.BookDtoConverter
import ru.revseev.library.view.CommentDtoConverter
import ru.revseev.library.view.dto.BookDto
import ru.revseev.library.view.dto.GenreDto
import ru.revseev.library.view.dto.toDto

private val log = KotlinLogging.logger { }

@Controller
class BookViewController(
    private val bookService: BookService,
    private val commentService: CommentService,
    private val genreService: GenreService,
    private val bookDtoConverter: BookDtoConverter,
    private val commentDtoConverter: CommentDtoConverter,
) {

    @GetMapping("/book/all")
    fun allBooks(): ModelAndView {
        log.info { "GET: /book/all" }
        val allBooks = bookService.getAll().map { bookDtoConverter.toDto(it) }
        return ModelAndView("books").addObject("allBooks", allBooks)
    }

    @GetMapping("/book/delete")
    fun delete(@RequestParam id: String): View {
        log.info { "GET: /book/delete?id=$id" }
        bookService.deleteById(id)
        return RedirectView("/book/all")
    }

    @GetMapping("/book/edit")
    fun toEditForm(@RequestParam id: String): ModelAndView {
        log.info { "GET: /book/edit?id=$id" }
        return ModelAndView("form")
            .addObject("bookDto", bookService.getById(id).let { bookDtoConverter.toDto(it) })
            .addObject("edit", true)
    }

    @PostMapping("/book/update")
    fun update(@ModelAttribute("bookDto") bookDto: BookDto): View {
        log.info { "POST: /book/update : $bookDto" }
        bookDtoConverter.toUpdatedBookDto(bookDto).let { bookService.update(it) }
        return RedirectView("/book/all")
    }

    @PostMapping("/book/add")
    fun add(@ModelAttribute("bookDto") bookDto: BookDto): View {
        log.info { "POST: /book/add : $bookDto" }
        bookDtoConverter.toNewBookDto(bookDto).let { bookService.add(it) }
        return RedirectView("/book/all")
    }

    @GetMapping("/book/new")
    fun toAddForm(): ModelAndView {
        log.info { "GET: /book/new" }
        return ModelAndView("form")
            .addObject("bookDto", BookDto())
            .addObject("edit", false)
    }

    @GetMapping("/book/comments")
    fun commentsByBookId(@RequestParam id: String): ModelAndView {
        log.info { "GET: /book/comments?id=$id" }
        val commentDtos = commentService.getByBookId(id).map { commentDtoConverter.toDto(it) }
        return ModelAndView("comments")
            .addObject("comments", commentDtos)
    }

    @ModelAttribute("allGenres")
    fun allGenres(): List<GenreDto> = genreService.getAll().map { it.toDto() }
}