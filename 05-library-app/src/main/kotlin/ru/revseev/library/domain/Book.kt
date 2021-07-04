package ru.revseev.library.domain

import javax.persistence.*

@Entity
@Table(name = "books")
class Book(
    @Id
    var id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String?,

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne(cascade = [CascadeType.PERSIST], optional = false)
    var author: Author?,

    @JoinTable(name = "books_genres",
        joinColumns = [JoinColumn(name = "book_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")])
    @ManyToMany
    var genres: MutableList<Genre> = mutableListOf(),
)