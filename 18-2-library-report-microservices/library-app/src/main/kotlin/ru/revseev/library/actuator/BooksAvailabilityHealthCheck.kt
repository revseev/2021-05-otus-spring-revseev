package ru.revseev.library.actuator

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component
import ru.revseev.library.repo.BookRepo

@Component
class BooksAvailabilityHealthCheck(private val bookRepo: BookRepo) : HealthIndicator {

    override fun health(): Health = try {
        val booksInDb = bookRepo.count()
        if (booksInDb == 0L) {
            Health.down()
                .withDetail("message", "WARNING: No books in the Library")
                .build()
        } else {
            Health.up()
                .withDetail("message", "OK: Books are in place")
                .build()
        }
    } catch (ex: Exception) {
        Health.outOfService()
            .withDetail("message", "WARNING: Cannot connect to the DB")
            .build()
    }
}