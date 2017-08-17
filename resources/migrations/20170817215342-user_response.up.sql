CREATE TABLE IF NOT EXISTS user_response
(id INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
user int unsigned not null,
topic int unsigned not null,
points int not null,
date DATE not null,
FOREIGN KEY (user) REFERENCES users(id),
FOREIGN KEY (topic) REFERENCES topics(id));

