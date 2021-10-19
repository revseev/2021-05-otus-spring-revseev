package ru.revseev.library.repo

import org.springframework.data.mongodb.repository.MongoRepository
import ru.revseev.library.domain.User

interface UserRepo : MongoRepository<User, String> {

    fun findByUsername(username: String): User?
}
