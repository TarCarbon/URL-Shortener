CREATE TABLE IF NOT EXISTS users (
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE CHECK (LENGTH(username) >= 2),
    password VARCHAR(100) NOT NULL,
    role     VARCHAR(20)  NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS urls (
    id              BIGSERIAL PRIMARY KEY,
    short_url       VARCHAR(20) NOT NULL,
    url             VARCHAR(255) NOT NULL,
    description     VARCHAR(1000),
    user_id         BIGSERIAL    NOT NULL,
    created_date    TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expiration_date TIMESTAMP  NOT NULL,
    visit_count     INT        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
