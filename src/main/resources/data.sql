-- noinspection SqlNoDataSourceInspectionForFile
INSERT INTO rbs_users (id, username, password, name) VALUES (1, 'admin', 'admin', 'Stephan Kemper');
INSERT INTO rbs_users (id, username, password, name) VALUES (2, 'testuser', 'test', 'Lukas Werner');

INSERT INTO rbs_roles (id, parent_id, name) VALUES (1, null, 'admin');
INSERT INTO rbs_roles (id, parent_id, name) VALUES (2, 1, 'user');

INSERT INTO rbs_users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO rbs_users_roles (user_id, role_id) VALUES (2, 2);

INSERT INTO rbs_authors (id, name) VALUES (1, 'Stephen King');
INSERT INTO rbs_authors (id, name) VALUES (2, 'Dan Brown');

INSERT INTO rbs_books (id, isbn, title, author_id) VALUES (3, '3404130081', 'Shining', 1);
INSERT INTO rbs_books (id, isbn, title, author_id) VALUES (4, '9788804560753', 'Meteor', 2);

INSERT INTO rbs_objects (id, table_name) VALUES (1, 'rbs_authors');
INSERT INTO rbs_objects (id, table_name) VALUES (2, 'rbs_authors');
INSERT INTO rbs_objects (id, table_name) VALUES (3, 'rbs_books');
INSERT INTO rbs_objects (id, table_name) VALUES (4, 'rbs_books');


INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete)
VALUES (1, 1, true, true, true);
INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete)
VALUES (1, 2, true, true, true);
INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete)
VALUES (1, 3, true, true, true);
INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete)
VALUES (1, 4, true, true, true);
INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete)
VALUES (2, 1, true, true, true);
INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete)
VALUES (2, 2, true, true, true);
INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete)
VALUES (2, 3, true, false, false);
INSERT INTO rbs_roles_objects (role_id, object_id, can_read, can_write, can_delete)
VALUES (2, 4, true, false, true);