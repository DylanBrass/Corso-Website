SET MODE MYSQL;
DROP TABLE IF EXISTS `gallery`;
DROP TABLE IF EXISTS `services`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `faqs`;
DROP TABLE IF EXISTS `reviews`;
DROP TABLE IF EXISTS `customers`;


CREATE TABLE IF NOT EXISTS `gallery`
(
    id
                   INT
                                      NOT
                                          NULL
        AUTO_INCREMENT
        PRIMARY
            KEY,
    gallery_id
                   VARCHAR(36) UNIQUE NOT NULL,
    description    VARCHAR(255)       NOT NULL,
    photo          VARCHAR(255)       NOT NULL,
    image_position INT                NOT NULL
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
                        VARCHAR(36)  NOT NULL,
    service_name        VARCHAR(255) NOT NULL,
    service_description TEXT         NOT NULL,
    service_icon        VARCHAR(255) NOT NULL,
    service_image       VARCHAR(255) NOT NULL,
    is_active           BOOL         NOT NULL

);

CREATE TABLE IF NOT EXISTS `orders`
(
    id
                              INT
                                           NOT
                                               NULL
        AUTO_INCREMENT
        PRIMARY
            KEY,
    order_id
                              varchar(36)  not null,
    service_id                varchar(36)  not null,
    user_id                   varchar(36)  not null,
    progress_information      text         null,
    order_tracking_number     varchar(36)  not null,
    order_date                date         not null,
    due_date                  date,
    order_status              varchar(50)  not null,
    order_description         text         null,
    customer_full_name        varchar(255) not null,
    customer_email            varchar(255) not null,
    customer_phone            varchar(255),
    customer_address          text         not null,
    customer_postal_code      varchar(10)  not null,
    customer_city             varchar(100) not null,
    customer_apartment_number varchar(25),
    hours_worked              int,
    estimated_duration        int
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
               VARCHAR(36) NOT NULL,
    question   TEXT        NOT NULL,
    answer     TEXT        NOT NULL,
    preference BOOL        NOT NULL
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
                       VARCHAR(36)  NOT NULL,
    user_id            VARCHAR(36)  NOT NULL,
    customer_full_name VARCHAR(255) NOT NULL,
    review_date        DATE         NOT NULL,
    message            TEXT         NOT NULL,
    review_rating      INT CHECK
        (
        review_rating >= 1 AND review_rating <= 5
        )                           NOT NULL,
    pinned             BOOL
);


CREATE TABLE IF NOT EXISTS `customers`
(
    id
                     int
        auto_increment
        primary
            key,
    user_id
                     varchar(40)  not null,
    name             varchar(100) null,
    email            varchar(100) not null,
    phone            varchar(100) null,
    address          varchar(100) not null,
    postal_code      varchar(10)  not null,
    apartment_number varchar(15)  null,
    city             varchar(60)  not null,
    verified      bool         not null

);

CREATE TABLE IF NOT EXISTS  `verification_token`
(
    id          int auto_increment
        primary key,
    token       varchar(50)  not null,
    email       varchar(100) not null,
    user_id     varchar(50)  not null,
    expiry_date datetime     not null
);

