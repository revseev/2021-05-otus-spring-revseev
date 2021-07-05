package ru.revseev.library.domain

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false, updatable = false)
    var book: Book,

    @Column(name = "body", nullable = false, length = 4096)
    var body: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Comment

        return id != null && id == other.id
    }

    override fun hashCode(): Int = 860659860
}