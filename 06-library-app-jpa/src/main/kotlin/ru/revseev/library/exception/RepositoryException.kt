package ru.revseev.library.exception

open class RepositoryException : GenericLibraryException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}