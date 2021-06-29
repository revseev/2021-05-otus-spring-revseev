INSERT INTO genre
values (1, 'Genre1'),
       (2, 'Genre2')
;
INSERT INTO author
values (1, 'Author1'),
       (2, 'Author2')
;
INSERT INTO book
VALUES (1, 'Book1',
        SELECT id FROM author WHERE name = 'Author1'),
       (2, 'Book2',
        SELECT id FROM author WHERE name = 'Author2')
;
INSERT INTO book_genre (book_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (2, 2)
;
