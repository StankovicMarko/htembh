CREATE TABLE IF NOT EXISTS users
(id bigserial PRIMARY KEY,
 registered timestamp NOT NULL,
 email VARCHAR(60) UNIQUE NOT NULL,
 pass VARCHAR(300) NOT NULL);
