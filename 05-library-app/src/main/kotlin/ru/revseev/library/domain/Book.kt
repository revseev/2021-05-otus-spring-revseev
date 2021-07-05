package ru.revseev.library.domain

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "books")
class Book(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne(cascade = [CascadeType.PERSIST], optional = false)
    var author: Author,

    @JoinTable(name = "books_genres",
        joinColumns = [JoinColumn(name = "book_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")])
    @ManyToMany
    var genres: MutableList<Genre> = mutableListOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Book

        return id != null && id == other.id
    }

    override fun hashCode(): Int = 967762358
}