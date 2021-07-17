package ru.revseev.library.repo

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import ru.revseev.library.domain.Book

interface BookRepo : JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = ["author", "genres"])
    override fun findAll(): List<Book>
}
