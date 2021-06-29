package ru.revseev.library.dao

import org.springframework.jdbc.core.ResultSetExtractor
import ru.revseev.library.domain.Book


interface BookDao {

    fun getAll(): List<Book>

    fun getById(id: Long): Book

    fun add(book: Book): Boolean

    fun update(book: Book): Boolean

    fun deleteById(id: Long): Boolean

//    fun <T> ResultSetExtractor(): ResultSetExtractor<T>

}