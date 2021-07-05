package ru.revseev.library.domain

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "genres")
class Genre(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "name", nullable = false, unique = true)
    var name: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Genre

        return id != null && id == other.id
    }

    override fun hashCode(): Int = 1887069089
}

