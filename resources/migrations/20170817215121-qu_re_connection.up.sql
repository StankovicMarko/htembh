CREATE TABLE IF NOT EXISTS qu_re_connection
(id bigserial PRIMARY KEY,
question bigserial not null,
response bigserial not null,
FOREIGN KEY (question) REFERENCES questions(id),
FOREIGN KEY (response) REFERENCES responses(id));

