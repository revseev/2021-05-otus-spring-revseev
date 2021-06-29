INSERT INTO genre (NAME)
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
INSERT INTO author (NAME)
values ('Philip K. Dick'),
       ('Antoine de Saint-Exupéry'),
       ('J. R. R. Tolkien'),
       ('J. K. Rowling'),
       ('Agatha Christie'),
       ('Umberto Eco'),
       ('H. P. Lovecraft')
;
INSERT INTO book(name, author_id)
VALUES ('Do Androids Dream of Electric Sheep?',
        SELECT id FROM author WHERE name = 'Philip K. Dick'),
       ('The Little Prince',
        SELECT id FROM author WHERE name = 'Antoine de Saint-Exupéry'),
       ('The Hobbit',
        SELECT id FROM author WHERE name = 'J. R. R. Tolkien')
;
INSERT INTO book_genre (book_id, genre_id)
VALUES (SELECT id FROM book WHERE name = 'The Hobbit',
        SELECT id FROM genre WHERE name = 'Adventure'),
       (SELECT id FROM book WHERE name = 'The Hobbit',
        SELECT id FROM genre WHERE name = 'Fiction'),
       (SELECT id FROM book WHERE name = 'The Hobbit',
        SELECT id FROM genre WHERE name = 'Fantasy'),
       (SELECT id FROM book WHERE name = 'The Little Prince',
        SELECT id FROM genre WHERE name = 'Fantasy'),
       (SELECT id FROM book WHERE name = 'The Little Prince',
        SELECT id FROM genre WHERE name = 'Novel'),
       (SELECT id FROM book WHERE name = 'Do Androids Dream of Electric Sheep?',
        SELECT id FROM genre WHERE name = 'Science Fiction'),
       (SELECT id FROM book WHERE name = 'Do Androids Dream of Electric Sheep?',
        SELECT id FROM genre WHERE name = 'Novel')
;
