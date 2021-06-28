package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import ru.revseev.library.dao.AuthorDao
import ru.revseev.library.domain.Author
import ru.revseev.library.service.AuthorService

@Service
class AuthorServiceImpl(private val authorDao: AuthorDao) : AuthorService {

    override fun getAll(): List<Author> = authorDao.getAll()

    override fun getById(id: Long): Author = authorDao.getById(id)

    override fun add(author: Author): Long = authorDao.add(author)

    override fun update(author: Author): Boolean = authorDao.update(author)

    override fun deleteById(id: Long): Boolean = authorDao.deleteById(id)
}