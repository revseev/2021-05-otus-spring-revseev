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
INSERT INTO books(title, author_id)
VALUES ('Do Androids Dream of Electric Sheep?',
           SELECT id FROM authors WHERE name = 'Philip K. Dick'),
       ('The Little Prince',
           SELECT id FROM authors WHERE name = 'Antoine de Saint-Exupéry'),
       ('The Hobbit',
           SELECT id FROM authors WHERE name = 'J. R. R. Tolkien')
;
INSERT INTO book_genres (book_id, genre_id)
VALUES (SELECT id FROM books WHERE title = 'The Hobbit',
           SELECT id FROM genres WHERE name = 'Adventure'),
       (SELECT id FROM books WHERE title = 'The Hobbit',
           SELECT id FROM genres WHERE name = 'Fiction'),
       (SELECT id FROM books WHERE title = 'The Hobbit',
           SELECT id FROM genres WHERE name = 'Fantasy'),
       (SELECT id FROM books WHERE title = 'The Little Prince',
           SELECT id FROM genres WHERE name = 'Fantasy'),
       (SELECT id FROM books WHERE title = 'The Little Prince',
           SELECT id FROM genres WHERE name = 'Novel'),
       (SELECT id FROM books WHERE title = 'Do Androids Dream of Electric Sheep?',
           SELECT id FROM genres WHERE name = 'Science Fiction'),
       (SELECT id FROM books WHERE title = 'Do Androids Dream of Electric Sheep?',
           SELECT id FROM genres WHERE name = 'Novel')
;
INSERT INTO comments (book_id, body)
VALUES ((SELECT id FROM books WHERE title = 'The Hobbit'), 'Greatest Adventure ever!'),
       ((SELECT id FROM books WHERE title = 'The Hobbit'), 'Loved it!'),
       ((SELECT id FROM books WHERE title = 'Do Androids Dream of Electric Sheep?'),
        'I decided to read it after Blade Runner movie.')
;