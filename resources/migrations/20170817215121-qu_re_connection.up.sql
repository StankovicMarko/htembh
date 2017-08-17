CREATE TABLE IF NOT EXISTS qu_re_connection
(id INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
question int unsigned not null,
response int unsigned not null,
FOREIGN KEY (question) REFERENCES questions(id),
FOREIGN KEY (response) REFERENCES responses(id));

