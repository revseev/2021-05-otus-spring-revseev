package ru.revseev.library.domain

import javax.persistence.*

@Entity
@Table(name = "comments")
class Comment(
    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false, updatable = false)
    val book: Book,

    @Column(name = "body", nullable = false, length = 4096)
    var body: String = "",
) : LongIdentifiable() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Comment) return false

        return id != null && id == other.id
    }

    override fun hashCode(): Int = 860659860

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , book = $book , body = $body )"
    }
}