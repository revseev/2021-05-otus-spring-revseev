DROP TABLE IF EXISTS book_genre;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;

CREATE TABLE genre
(
    id   LONG         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE author
(
    id   LONG         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE book
(
    id        LONG         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    author_id LONG         NOT NULL,
    FOREIGN KEY (author_id) REFERENCES author (id)
);

CREATE TABLE book_genre
(
    id       LONG NOT NULL PRIMARY KEY AUTO_INCREMENT,
    book_id  LONG NOT NULL,
    genre_id LONG NOT NULL,
    FOREIGN KEY (book_id) REFERENCES book (id),
    FOREIGN KEY (genre_id) REFERENCES genre (id)
);