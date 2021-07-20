package ru.revseev.library.repo

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import ru.revseev.library.domain.Book
import java.util.*

interface BookRepo : JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = ["author", "genres"])
    override fun findAll(): List<Book>

    @EntityGraph(attributePaths = ["author", "genres"])
    override fun findById(id: Long): Optional<Book>
}
