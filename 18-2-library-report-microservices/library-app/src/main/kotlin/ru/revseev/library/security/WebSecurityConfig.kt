package ru.revseev.library.security

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http {
            authorizeRequests {
                authorize("/login", permitAll)
                authorize("/book/all", hasAnyAuthority("USER", "ADMIN"))
                authorize("/book/**", hasAuthority("ADMIN"))
                authorize("/api/v1/books", hasAuthority("USER"))
                authorize("/api/**", hasAuthority("ADMIN"))
                authorize("/actuator/**", hasAuthority("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            formLogin { loginPage = "/login" }
            logout { logoutSuccessUrl = "/login" }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

}