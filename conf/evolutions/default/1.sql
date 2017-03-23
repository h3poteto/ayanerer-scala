# Ayanerus schema

# --- !Ups
CREATE TABLE IF NOT EXISTS ayanerus(
id int(11) NOT NULL AUTO_INCREMENT,
image varchar(255) DEFAULT NULL,
created_at datetime DEFAULT NULL,
updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY(id),
UNIQUE INDEX index_on_image (image))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

# --- !Downs
DROP TABLE ayanerus;
