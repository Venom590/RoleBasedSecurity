-- noinspection SqlNoDataSourceInspectionForFile
INSERT INTO rbs_users (id, username, password, name) VALUES (1, 'admin', 'admin', 'Stephan Kemper');
INSERT INTO rbs_users (id, username, password, name) VALUES (2, 'testuser', 'test', 'Lukas Werner');

INSERT INTO rbs_roles (id, parent_id, name) VALUES (1, null, 'admin');
INSERT INTO rbs_roles (id, parent_id, name) VALUES (2, 1, 'user');

INSERT INTO rbs_users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO rbs_users_roles (user_id, role_id) VALUES (2, 2);

INSERT INTO rbs_objects (id, name) VALUES (1, 'Object 1');
INSERT INTO rbs_objects (id, name) VALUES (2, 'Object 2');
INSERT INTO rbs_objects (id, name) VALUES (3, 'Object 3');

INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete) VALUES (1, 1, 1, 1, 1);
INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete) VALUES (1, 2, 1, 1, 0);
INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete) VALUES (2, 1, 1, 0, 0);
INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete) VALUES (2, 2, 0, 0, 0);