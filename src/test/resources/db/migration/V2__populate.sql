INSERT INTO users (username, password, role)
VALUES ('testuser', 'password', 'USER');

INSERT INTO urls (short_url, url, description, created_date, expiration_date, visit_count)
VALUES ('testurl', 'https://some_long_named_portal.com/', 'for test only', '2024-01-18 12:34:56','2024-02-18 12:34:56', 1);