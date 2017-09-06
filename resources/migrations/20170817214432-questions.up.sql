CREATE TABLE IF NOT EXISTS questions
(id bigserial PRIMARY KEY,
topic bigserial not null,
text varchar(300) not null,
FOREIGN KEY (topic) REFERENCES topics(id));
