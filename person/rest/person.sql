drop table if exists person;

CREATE TABLE IF NOT EXISTS person ( 
    p_key INTEGER PRIMARY KEY,
    p_name VARCHAR(25) NOT NULL
    );


INSERT INTO person (p_key, p_name) VALUES (1, "Jakob Pogulis");
INSERT INTO person (p_key, p_name) VALUES (2, "Xena");
INSERT INTO person (p_key, p_name) VALUES (3, "Macus Brown");
INSERT INTO person (p_key, p_name) VALUES (4, "Zorro");
INSERT INTO person (p_key, p_name) VALUES (5, "Q");
