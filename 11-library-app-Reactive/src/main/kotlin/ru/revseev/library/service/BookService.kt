package ru.revseev.library.service

import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Sort
import ru.revseev.library.domain.Book
import ru.revseev.library.view.dto.NewBookDto
import ru.revseev.library.view.dto.UpdatedBookDto

interface BookService {

    suspend fun getAll(): Flow<Book>

    suspend fun getAll(sort: Sort): Flow<Book>

    suspend fun getById(id: String): Book

    suspend fun add(dto: NewBookDto): Book

    suspend fun update(dto: UpdatedBookDto): Book

    suspend fun deleteById(id: String): Boolean
}
