CREATE TABLE IF NOT EXISTS user_responses
(id bigserial PRIMARY KEY,
email VARCHAR(60) NOT NULL,
topic bigserial not null,
points bigserial not null,
submitted timestamp not null,
FOREIGN KEY (topic) REFERENCES topics(id));
