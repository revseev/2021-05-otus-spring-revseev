INSERT INTO genres
values (1, 'Genre1'),
       (2, 'Genre2'),
       (3, 'Genre3')
;
INSERT INTO authors
values (1, 'Author1'),
       (2, 'Author2')
;
INSERT INTO books
VALUES (1, 'Book1',
        SELECT id FROM authors WHERE name = 'Author1'),
       (2, 'Book2',
        SELECT id FROM authors WHERE name = 'Author2')
;
INSERT INTO book_genre (book_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (2, 2)
;
