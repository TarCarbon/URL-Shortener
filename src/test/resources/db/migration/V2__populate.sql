INSERT INTO users (username, password, role)
VALUES  ('testadmin', 'qwerTy12', 'ADMIN'),
		('testuser1', 'qwerTy12', 'USER'),
		('testuser2', 'qwerTy12', 'USER');

INSERT INTO urls (short_url, url, description, user_id, created_date, expiration_date, visit_count)
VALUES  ('testurl1', 'https://some_long_named_portal.com/', 'for test only', 1, '2024-01-18 12:34:56','2024-02-18 12:34:56', 1),
		('testurl2', 'https://some_long_named_portal.com/', 'for test only', 1, '2024-01-18 12:34:56','2024-02-18 12:34:56', 1),
		('testurl3', 'https://some_long_named_portal.com/', 'for test only', 2, '2024-01-18 12:34:56','2024-02-18 12:34:56', 1),
		('testurl4', 'https://some_long_named_portal.com/', 'for test only', 3, '2024-01-18 12:34:56','2024-02-18 12:34:56', 1);