package ru.revseev.library.security

import mu.KotlinLogging
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.revseev.library.service.UserService

val log = KotlinLogging.logger { }

@Service
class SimpleUserDetailsService(private val userService: UserService) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        log.info { "Authenticating username: $username ..." }
        val user = userService.findByUsername(requireNotNull(username) { "Username cannot be null" })

        return SimpleUserDetails(requireNotNull(user) { "User '$username' is not found" })
    }
}