INSERT INTO genres (NAME)
values ('Action'),
       ('Adventure'),
       ('Biography'),
       ('Classics'),
       ('Detective'),
       ('Fantasy'),
       ('Fiction'),
       ('Horror'),
       ('Non-Fiction'),
       ('Novel'),
       ('Romance'),
       ('Science Fiction'),
       ('Thriller')
;
INSERT INTO authors (NAME)
values ('Philip K. Dick'),
       ('Antoine de Saint-Exupéry'),
       ('J. R. R. Tolkien'),
       ('J. K. Rowling'),
       ('Agatha Christie'),
       ('Umberto Eco'),
       ('H. P. Lovecraft')
;
INSERT INTO books(name, author_id)
VALUES ('Do Androids Dream of Electric Sheep?',
        SELECT id FROM authors WHERE name = 'Philip K. Dick'),
       ('The Little Prince',
        SELECT id FROM authors WHERE name = 'Antoine de Saint-Exupéry'),
       ('The Hobbit',
        SELECT id FROM authors WHERE name = 'J. R. R. Tolkien')
;
INSERT INTO book_genre (book_id, genre_id)
VALUES (SELECT id FROM books WHERE name = 'The Hobbit',
        SELECT id FROM genres WHERE name = 'Adventure'),
       (SELECT id FROM books WHERE name = 'The Hobbit',
        SELECT id FROM genres WHERE name = 'Fiction'),
       (SELECT id FROM books WHERE name = 'The Hobbit',
        SELECT id FROM genres WHERE name = 'Fantasy'),
       (SELECT id FROM books WHERE name = 'The Little Prince',
        SELECT id FROM genres WHERE name = 'Fantasy'),
       (SELECT id FROM books WHERE name = 'The Little Prince',
        SELECT id FROM genres WHERE name = 'Novel'),
       (SELECT id FROM books WHERE name = 'Do Androids Dream of Electric Sheep?',
        SELECT id FROM genres WHERE name = 'Science Fiction'),
       (SELECT id FROM books WHERE name = 'Do Androids Dream of Electric Sheep?',
        SELECT id FROM genres WHERE name = 'Novel')
;
