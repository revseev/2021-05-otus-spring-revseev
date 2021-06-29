DROP TABLE IF EXISTS book_genre;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

CREATE TABLE genres
(
    id   LONG         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE authors
(
    id   LONG         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE books
(
    id        LONG         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title     VARCHAR(255) NOT NULL,
    author_id LONG         NOT NULL,
    FOREIGN KEY (author_id) REFERENCES authors (id)
);

CREATE TABLE book_genre
(
    id       LONG NOT NULL PRIMARY KEY AUTO_INCREMENT,
    book_id  LONG NOT NULL,
    genre_id LONG NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id)
);