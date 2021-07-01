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
}