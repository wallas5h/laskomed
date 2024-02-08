INSERT INTO app_user (user_id ,username, password, email, enabled, confirmed) VALUES
(1, 'john.doe', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'john.doe@example.com', true, true),
(2, 'jane.smith', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'jane.smith@example.com', true, true),
(3, 'adam.mocny', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'adam.mocny@example.com', true, true),
(4, 'michal.podoba', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'michal.podoba@example.com', true, true),
(5, 'barbara.racka', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'barbara.racka@example.com', true, true),
(6, 'robert.miler', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'robert.miler@example.com', true, true),
(7, 'jan.kiler', '$2a$10$gqHrslMttQWSsDSVRTK1OehkkBiXsJ/a4z2OURU./dizwOQu5Lovu', 'jan.kiler@example.com', true, true),
(8, 'mike.johnson', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'mike.johnson@example.com', true, true),
(9, 'alice.williams', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'alice.williams@example.com', true, true),
(10, 'bob.miller', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'bob.miller@example.com', true, true);

INSERT INTO role VALUES
(1,'ROLE_PATIENT'),
(2,'ROLE_DOCTOR'),
(3,'ROLE_ADMIN');

INSERT INTO app_user_role (user_id, role_id) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 2),
(5, 2),
(6, 2),
(7, 2),
(8, 1),
(9, 1),
(10, 1);






