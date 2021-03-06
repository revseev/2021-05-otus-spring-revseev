package ru.revseev.library.domain

import org.hibernate.Hibernate
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import javax.persistence.*
import javax.persistence.CascadeType.MERGE
import javax.persistence.CascadeType.PERSIST

@Entity
@Table(name = "books")
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

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "book", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<Comment> = mutableListOf()

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