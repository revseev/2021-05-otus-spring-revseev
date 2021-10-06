package ru.revseev.library.view.rest

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.revseev.library.domain.Book
import ru.revseev.library.exception.BadRequestException
import ru.revseev.library.service.BookService
import ru.revseev.library.view.BookDtoConverter
import ru.revseev.library.view.dto.BookDto

private val log = KotlinLogging.logger { }

@CrossOrigin
@RestController
class BookRestController(
    private val bookService: BookService,
    private val bookDtoConverter: BookDtoConverter,
) {

    @GetMapping("/api/v1/books")
    suspend fun getAll(
        @RequestParam(defaultValue = "0") offset: Int,
        @RequestParam(defaultValue = "10") limit: Int,
    ): Flow<BookDto> {
        log.info { "GET: /api/v1/books : offset=$offset, limit=$limit" }
        val sort = Sort.by(Sort.Direction.DESC, "id")
        return bookService.getAll(sort).map { it.toDto() }
    }

    @GetMapping("/api/v1/books/{id}")
    suspend fun getById(@PathVariable id: String): BookDto {
        log.info { "GET: /api/v1/books/$id" }
        return bookService.getById(id).toDto()
    }

    @PostMapping("/api/v1/books")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun add(@RequestBody bookDto: BookDto): BookDto {
        log.info { "POST: /api/v1/books : $bookDto" }
        return bookService.add(bookDtoConverter.toNewBookDto(bookDto)).toDto()
    }

    @PutMapping("/api/v1/books/{id}")
    suspend fun update(@PathVariable id: String, @RequestBody bookDto: BookDto): BookDto {
        log.info { "PUT: /api/v1/books/$id : $bookDto" }
        assertSameId(bookDto, id)
        return bookService.update(bookDtoConverter.toUpdatedBookDto(bookDto)).toDto()
    }

    @DeleteMapping("/api/v1/books/{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<*> {
        log.info { "DELETE: /api/v1/books/$id" }
        return if (bookService.deleteById(id)) {
            ResponseEntity.ok()
        } else {
            ResponseEntity.notFound()
        }.build<Nothing>()
    }


    private fun assertSameId(bookDto: BookDto, id: String) {
        if (id != bookDto.id) {
            throw BadRequestException("The ID in updated entity must match the id in URI ($id = ${bookDto.id})")
        }
    }

    private fun Book.toDto(): BookDto = bookDtoConverter.toDto(this)

}
