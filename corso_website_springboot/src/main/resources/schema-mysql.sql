USE `corso-dev-db`;

CREATE TABLE IF NOT EXISTS gallery (
    id INT NOT NULL AUTO_INCREMENT,
    gallery_id VARCHAR(36) UNIQUE NOT NULL,
    description VARCHAR(255) NOT NULL,
    photo VARCHAR(255) NOT NULL,
    image_position INT UNIQUE NOT NULL,
    PRIMARY KEY (id)
);