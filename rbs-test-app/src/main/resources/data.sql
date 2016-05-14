-- noinspection SqlNoDataSourceInspectionForFile
INSERT INTO rbs_users (id, username, password, name) VALUES (1, 'admin', 'admin', 'Stephan Kemper');
INSERT INTO rbs_users (id, username, password, name) VALUES (2, 'testuser', 'test', 'Lukas Werner');

INSERT INTO rbs_roles (id, parent_id, name) VALUES (1, null, 'admin');
INSERT INTO rbs_roles (id, parent_id, name) VALUES (2, 1, 'user');

INSERT INTO rbs_users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO rbs_users_roles (user_id, role_id) VALUES (2, 2);