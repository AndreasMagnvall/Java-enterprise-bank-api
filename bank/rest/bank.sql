drop table if exists bank;

CREATE TABLE IF NOT EXISTS bank ( 
    p_key INTEGER PRIMARY KEY,
    p_name VARCHAR(25) NOT NULL
    );

INSERT INTO bank (p_key, p_name) VALUES (1, "SWEDBANK");
INSERT INTO bank (p_key, p_name) VALUES (2, "IKANOBANKEN");
INSERT INTO bank (p_key, p_name) VALUES (3, "JPMORGAN");
INSERT INTO bank (p_key, p_name) VALUES (4, "NORDEA");
INSERT INTO bank (p_key, p_name) VALUES (5, "CITIBANK");
INSERT INTO bank (p_key, p_name) VALUES (6, "HANDELSBANKEN");
INSERT INTO bank (p_key, p_name) VALUES (7, "SBAB");
INSERT INTO bank (p_key, p_name) VALUES (8, "HSBC");
INSERT INTO bank (p_key, p_name) VALUES (9, "NORDNET");
