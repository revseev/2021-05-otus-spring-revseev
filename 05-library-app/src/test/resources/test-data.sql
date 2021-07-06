INSERT INTO genres
values (1, 'Genre1'),
       (2, 'Genre2'),
       (3, 'Genre3')
;
INSERT INTO authors
values (1, 'Author1'),
       (2, 'Author2'),
       (3, 'Author3')
;
INSERT INTO books
VALUES (1, 'Book1', 1),
       (2, 'Book2', 2)
;
INSERT INTO book_genres (book_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (2, 2)
;
INSERT INTO comments (book_id, body)
VALUES (1, 'Comment1'),
       (1, 'Comment2'),
       (2, 'Comment2')
;
