-- noinspection SqlNoDataSourceInspectionForFile
CREATE TABLE rbs_roles (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  parent_id INT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE(name),
  FOREIGN KEY (parent_id) REFERENCES rbs_roles(id)
);

CREATE TABLE rbs_users (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(255),
  UNIQUE(username)
);

CREATE TABLE rbs_users_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES rbs_users(id),
  FOREIGN KEY (role_id) REFERENCES rbs_roles(id)
);