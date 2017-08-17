CREATE TABLE IF NOT EXISTS questions
(id INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
topic int unsigned not null,
text varchar(300) not null,
FOREIGN KEY (topic) REFERENCES topics(id));
