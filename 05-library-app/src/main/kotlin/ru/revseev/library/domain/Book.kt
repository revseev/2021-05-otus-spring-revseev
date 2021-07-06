package ru.revseev.library.domain

import org.hibernate.Hibernate
import javax.persistence.*
import javax.persistence.CascadeType.*

@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book-genre-graph", attributeNodes = [NamedAttributeNode("author")])
class Book(
    @Column(name = "title", nullable = false)
    var title: String,

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne(cascade = [PERSIST, MERGE], optional = false)
    val author: Author,

    @JoinTable(
        name = "book_genres",
        joinColumns = [JoinColumn(name = "book_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    @ManyToMany(cascade = [PERSIST, MERGE])
    var genres: MutableList<Genre> = mutableListOf(),
) : LongIdentifiable() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Book

        return id != null && id == other.id
    }

    override fun hashCode(): Int = 967762358

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , author = $author )"
    }
}