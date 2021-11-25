package ru.revseev.library.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.domain.User
import ru.revseev.library.exception.UserAlreadyExistsException
import ru.revseev.library.repo.UserRepo
import ru.revseev.library.service.UserService

@Service
class UserServiceImpl(private val userRepo: UserRepo) : UserService {

    @Transactional
    override fun save(user: User): User {
        val username = user.username

        if (userRepo.findByUsername(username) == null) {
            return userRepo.save(user)
        } else {
            throw UserAlreadyExistsException("User with username: $username already exists")
        }
    }

    @Transactional(readOnly = true)
    override fun findByUsername(username: String): User? = userRepo.findByUsername(username)

}