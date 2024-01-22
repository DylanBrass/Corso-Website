USE
`corso-dev-db`;

CREATE TABLE IF NOT EXISTS `gallery`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `gallery_id`
    VARCHAR
(
    36
) UNIQUE NOT NULL,
    `description` VARCHAR
(
    255
) NOT NULL,
    `photo` VARCHAR
(
    255
) NOT NULL,
    `image_position` INT UNIQUE NOT NULL,
    PRIMARY KEY
(
    `id`
)
    );

CREATE TABLE IF NOT EXISTS `services`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT
    PRIMARY
    KEY,
    `service_id`
    VARCHAR
(
    36
) NOT NULL,
    `service_name` VARCHAR
(
    255
) NOT NULL,
    `service_description` TEXT NOT NULL,
    `service_icon` VARCHAR
(
    255
) NOT NULL,
    `service_image` VARCHAR
(
    255
) NOT NULL,
    is_active tinyint not null,
    INDEX
(
    `service_id`
)
    );

CREATE TABLE IF NOT EXISTS `orders`
(
    `id`
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    `order_id`
    VARCHAR
(
    36
) NOT NULL,
    `service_id` VARCHAR
(
    36
) NOT NULL,
    `user_id` VARCHAR
(
    36
) NOT NULL,
    `order_tracking_number` VARCHAR
(
    36
) NOT NULL,
    `order_date` DATE NOT NULL,
    `due_date` DATE NULL,
    `order_status` VARCHAR
(
    50
) NOT NULL,
    `order_description` TEXT NULL,
    `customer_full_name` VARCHAR
(
    255
) NOT NULL,
    `customer_email` VARCHAR
(
    255
) NOT NULL,
    `customer_phone` VARCHAR
(
    255
) NULL,
    `customer_address` TEXT NOT NULL,
    `customer_postal_code` VARCHAR
(
    10
) NOT NULL,
    `customer_city` VARCHAR
(
    100
) NOT NULL,
    `customer_apartment_number` VARCHAR
(
    25
) NULL,
    `hours_worked` INT NULL,
    `estimated_duration` INT NULL,
    FOREIGN KEY
(
    `service_id`
) REFERENCES `services`
(
    `service_id`
)
    );

CREATE TABLE IF NOT EXISTS `faqs`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT
    PRIMARY
    KEY,
    `faq_id`
    VARCHAR
(
    36
) NOT NULL,
    `question` TEXT NOT NULL,
    `answer` TEXT NOT NULL,
    `preference` BOOL NOT NULL
    );

CREATE TABLE IF NOT EXISTS `reviews`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT
    PRIMARY
    KEY,
    `review_id`
    VARCHAR
(
    36
) NOT NULL,
    `user_id` VARCHAR
(
    36
) NOT NULL,
    `customer_full_name` VARCHAR
(
    255
) NOT NULL,
    `review_date` DATE NOT NULL,
    `message` TEXT NOT NULL,
    `review_rating` INT CHECK
(
    `review_rating`
    BETWEEN
    1
    AND
    5
) NOT NULL,
    `pinned` BOOL
    );
