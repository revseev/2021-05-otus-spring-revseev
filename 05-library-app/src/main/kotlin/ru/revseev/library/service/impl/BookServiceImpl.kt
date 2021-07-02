package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import ru.revseev.library.dao.BookDao
import ru.revseev.library.domain.Book
import ru.revseev.library.service.BookService

@Service
class BookServiceImpl(private val bookDao: BookDao) : BookService {

    override fun getAll(): List<Book> {
        return bookDao.getAll()
    }

    override fun getById(id: Long): Book {
        return bookDao.getById(id)
    }

    override fun add(book: Book): Long {
        return bookDao.add(book)

    }

    override fun update(book: Book): Boolean {
        return bookDao.update(book)

    }

    override fun deleteById(id: Long): Boolean {
        return bookDao.deleteById(id)
    }
}