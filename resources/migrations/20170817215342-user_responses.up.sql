CREATE TABLE IF NOT EXISTS user_responses
(id INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
user VARCHAR(60) NOT NULL,
topic int unsigned not null,
points int not null,
date timestamp not null,
FOREIGN KEY (topic) REFERENCES topics(id));

