-- noinspection SqlNoDataSourceInspectionForFile
DROP TABLE IF EXISTS rbs_roles;
DROP TABLE IF EXISTS rbs_users;
DROP TABLE IF EXISTS rbs_objects;
DROP TABLE IF EXISTS rbs_authors;
DROP TABLE IF EXISTS rbs_books;
DROP TABLE IF EXISTS rbs_users_roles;
DROP TABLE IF EXISTS rbs_roles_objects;

CREATE TABLE rbs_roles (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  parent_id INT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE(name),
  FOREIGN KEY (parent_id) REFERENCES rbs_roles(id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE rbs_users (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(255) NULL,
  UNIQUE(username)
);

CREATE TABLE rbs_objects (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE rbs_authors (
  id INT NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  FOREIGN KEY (id) REFERENCES rbs_objects(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE rbs_books (
  id INT NOT NULL PRIMARY KEY,
  isbn VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  author_id INT NULL,
  FOREIGN KEY (author_id) REFERENCES rbs_authors(id) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (id) REFERENCES rbs_objects(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE rbs_users_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES rbs_users(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (role_id) REFERENCES rbs_roles(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE rbs_roles_objects (
  role_id INT NOT NULL,
  object_id INT NOT NULL,
  can_read BOOLEAN NULL,
  can_write BOOLEAN NULL,
  can_delete BOOLEAN NULL,
  PRIMARY KEY (role_id, object_id),
  FOREIGN KEY (role_id) REFERENCES rbs_roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (object_id) REFERENCES rbs_objects(id) ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE rbs_roles
ADD CONSTRAINT chk_role CHECK (parent_id <> id)