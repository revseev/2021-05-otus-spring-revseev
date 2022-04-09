package ru.revseev.library.actuator

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component
import ru.revseev.library.repo.BookRepo

@Component
class BooksAvailabilityHealthCheck(private val bookRepo: BookRepo) : HealthIndicator {

    override fun health(): Health = when {
        bookRepo.count() == 0L -> Health.down()
            .withDetail("message", "WARNING: No books in the Library")
            .build()
        else -> Health.up()
            .withDetail("message", "OK: Books are in place")
            .build()
    }
}