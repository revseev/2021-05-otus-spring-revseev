package ru.revseev.library.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "authors")
class Author(
    @Column(name = "name", nullable = false)
    val name: String,
) : LongIdentifiable() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Author) return false

        return id != null && id == other.id
    }

    override fun hashCode(): Int = 556590234

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name )"
    }
}

