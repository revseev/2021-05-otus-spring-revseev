package ru.revseev.library.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "genres")
class Genre(
    @Column(name = "name", nullable = false, unique = true)
    val name: String,
) : LongIdentifiable() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Genre) return false

        return id != null && id == other.id
    }

    override fun hashCode(): Int = 1887069089

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name )"
    }
}

