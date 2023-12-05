CREATE TABLE IF NOT EXISTS gallery (
                                       id INT NOT NULL AUTO_INCREMENT,
                                       gallery_id VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    photo VARCHAR NOT NULL,
    image_position INT NOT NULL,
    PRIMARY KEY (id)
    );
