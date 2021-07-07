package ru.revseev.library.shell.impl

import org.springframework.stereotype.Service
import ru.revseev.library.domain.Comment
import ru.revseev.library.shell.CommentViewer

@Service
class CommentViewerImpl : CommentViewer {

    override fun viewList(comments: Collection<Comment>): String {
        TODO("Not yet implemented")
    }
}