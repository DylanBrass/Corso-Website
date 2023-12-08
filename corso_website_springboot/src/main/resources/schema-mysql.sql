USE `corso-dev-db`;

CREATE TABLE IF NOT EXISTS gallery (
    id INT NOT NULL AUTO_INCREMENT,
    gallery_id VARCHAR(36) UNIQUE NOT NULL,
    description VARCHAR(255) NOT NULL,
    photo VARCHAR(255) NOT NULL,
    image_position INT UNIQUE NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS `services`
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    service_id VARCHAR(36) NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    service_description TEXT NOT NULL,
    service_icon VARCHAR(255) NOT NULL,
    service_image VARCHAR(255) NOT NULL,
    INDEX (service_id)
    );

CREATE TABLE IF NOT EXISTS `orders`
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    service_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    order_date DATE NOT NULL,
    due_date DATE,
    order_status VARCHAR(50) NOT NULL,
    order_description TEXT NULL,
    customer_fullname VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    customer_phone VARCHAR(255),
    customer_address TEXT NOT NULL,
    FOREIGN KEY (service_id) REFERENCES services(service_id)
    );

CREATE TABLE IF NOT EXISTS  `FAQs`(
                                      id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                      faq_id VARCHAR(36) NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    preference BOOL NOT NULL
    );

CREATE TABLE IF NOT EXISTS `reviews`
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    review_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    customer_fullname VARCHAR(255) NOT NULL,
    review_date DATE NOT NULL,
    message TEXT NOT NULL,
    review_rating INT CHECK ( review_rating >= 1 AND review_rating <= 5 ) NOT NULL,
    pinned BOOL
    );