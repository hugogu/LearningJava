CREATE TABLE IF NOT EXISTS Users (
 userId bigint(20) NOT NULL AUTO_INCREMENT,
 name varchar(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS BOOK(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    ISBN VARCHAR(255),
    PRICE FLOAT,
    TITLE VARCHAR(255)
);