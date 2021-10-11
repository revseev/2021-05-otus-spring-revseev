package ru.revseev.library.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class SimpleUserDetailsService : UserDetailsService{

    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }
}