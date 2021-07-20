package ru.revseev.library.domain

import javax.persistence.*

@MappedSuperclass
abstract class LongIdentifiable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun isNew(): Boolean = id == null
}