package ru.revseev.library.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "authors")
class Author(
    @Id
    var id: Long? = null,
    @Column(name = "name", nullable = false, unique = true)
    var name: String?
)

