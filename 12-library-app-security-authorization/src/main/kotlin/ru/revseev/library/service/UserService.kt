package ru.revseev.library.service

import ru.revseev.library.domain.User

interface UserService {

    fun save(user: User): User

    fun findByUsername(username: String): User?
}