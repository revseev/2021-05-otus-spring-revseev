package ru.revseev.library.shell.impl

import org.springframework.stereotype.Service
import ru.revseev.library.domain.Comment
import ru.revseev.library.shell.CommentViewer

@Service
class CommentViewerImpl : CommentViewer {

    override fun viewList(comments: Collection<Comment>): String = comments.joinToString { view(it) }

    override fun view(comment: Comment): String =
        """[${comment.id}] Comment for book "${comment.book.title}" by "${comment.book.author.name}:
           |  "${comment.body}"
        """.trimMargin()
}