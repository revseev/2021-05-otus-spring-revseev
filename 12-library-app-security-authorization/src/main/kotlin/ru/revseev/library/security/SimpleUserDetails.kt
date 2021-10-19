package ru.revseev.library.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.revseev.library.domain.User

class SimpleUserDetails(private val user: User) : UserDetails {

    private val grantedAuthorities: MutableCollection<out GrantedAuthority> by lazy {
        user.roles.map { SimpleGrantedAuthority(it.name) }.toMutableList()
    }

    override fun getUsername(): String = user.username

    override fun getPassword(): String = user.password

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = grantedAuthorities

    override fun isEnabled(): Boolean = user.enabled

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

}