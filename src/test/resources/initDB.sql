DROP TABLE users IF EXISTS;

CREATE TABLE users
(
    id INTEGER PRIMARY KEY NOT NULL,
    name VARCHAR(32) NOT NULL,
    email VARCHAR(128) NOT NULL,
    password VARCHAR(32) NOT NULL
);

CREATE TABLE roles_users
(
    user_id INTEGER NOT NULL,
    role VARCHAR(255),
    CONSTRAINT roles_user_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id)
)

