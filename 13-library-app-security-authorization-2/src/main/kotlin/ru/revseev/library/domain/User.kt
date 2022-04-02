package ru.revseev.library.domain

data class User(
    val username: String,
    val password: String,
    val roles: MutableCollection<Role> = mutableListOf(),
    var enabled: Boolean = true,
    var email: String? = null,
) : StringIdentifiable()