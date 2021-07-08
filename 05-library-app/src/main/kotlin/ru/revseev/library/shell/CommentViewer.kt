package ru.revseev.library.shell

import ru.revseev.library.domain.Comment

interface CommentViewer {

    fun viewList(comments: Collection<Comment>): String

    fun view(comment: Comment): String
}
