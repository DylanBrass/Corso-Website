USE
`corso-dev-db`;

CREATE TABLE IF NOT EXISTS `gallery`
(
    id
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    gallery_id
    VARCHAR
(
    36
) UNIQUE NOT NULL,
    description VARCHAR
(
    255
) NOT NULL,
    photo VARCHAR
(
    255
) NOT NULL,
    image_position INT UNIQUE NOT NULL,
    PRIMARY KEY
(
    id
)
    );


CREATE TABLE IF NOT EXISTS `services`
(
    id
    INT
    NOT
    NULL
    AUTO_INCREMENT
    PRIMARY
    KEY,
    service_id
    VARCHAR
(
    36
) NOT NULL,
    service_name VARCHAR
(
    255
) NOT NULL,
    service_description TEXT NOT NULL,
    service_icon VARCHAR
(
    255
) NOT NULL,
    service_image VARCHAR
(
    255
) NOT NULL,
    INDEX
(
    service_id
)
    );

CREATE TABLE IF NOT EXISTS orders
(
    id                        int auto_increment
        primary key,
    order_id                  varchar(36)  not null,
    service_id                varchar(36)  not null,
    user_id                   varchar(36)  not null,
    order_tracking_number     varchar(36)  not null,
    order_date                date         not null,
    due_date                  date null,
    order_status              varchar(50)  not null,
    order_description         text null,
    customer_full_name        varchar(255) not null,
    customer_email            varchar(255) not null,
    customer_phone            varchar(255) null,
    customer_address          text         not null,
    customer_postal_code      varchar(10)  not null,
    customer_city             varchar(100) not null,
    customer_apartment_number varchar(25) null,
    days_left                 int null,
    estimated_duration        int null,
    FOREIGN KEY (service_id) REFERENCES services (service_id)
);

CREATE TABLE IF NOT EXISTS `faqs`
(
    id
    INT
    NOT
    NULL
    AUTO_INCREMENT
    PRIMARY
    KEY,
    faq_id
    VARCHAR
(
    36
) NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    preference BOOL NOT NULL
    );

CREATE TABLE IF NOT EXISTS `reviews`
(
    id
    INT
    NOT
    NULL
    AUTO_INCREMENT
    PRIMARY
    KEY,
    review_id
    VARCHAR
(
    36
) NOT NULL,
    user_id VARCHAR
(
    36
) NOT NULL,
    customer_full_name VARCHAR
(
    255
) NOT NULL,
    review_date DATE NOT NULL,
    message TEXT NOT NULL,
    review_rating INT CHECK
(
    review_rating
    >=
    1
    AND
    review_rating
    <=
    5
) NOT NULL,
    pinned BOOL
    );