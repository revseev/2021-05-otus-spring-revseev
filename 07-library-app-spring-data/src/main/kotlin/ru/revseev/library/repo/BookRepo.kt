package ru.revseev.library.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.revseev.library.domain.Book

interface BookRepo : JpaRepository<Book, Long> {

//    fun findAll(): List<Book>
//
//    fun findByIdOrNull(id: Long): Book?
//
//    fun save(book: Book): Book
//
    fun deleteBookById(id: Long): Boolean
}
