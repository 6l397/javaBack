CREATE TABLE t_reviews
(
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    UserName VARCHAR(255),
    Content VARCHAR(255),
    Rating INT(11),
    PRIMARY KEY (id)
);
