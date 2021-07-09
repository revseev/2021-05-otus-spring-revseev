package ru.revseev.library.shell

import ru.revseev.library.domain.Comment

interface CommentViewer {

    fun view(comment: Comment): String

    fun viewList(comments: Collection<Comment>): String
}
