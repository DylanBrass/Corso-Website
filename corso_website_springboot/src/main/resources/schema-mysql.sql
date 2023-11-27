USE `test-db`;

create table if not exists clients (
                         id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         client_id VARCHAR(36),
                         first_name VARCHAR(50),
                         last_name VARCHAR(50),
                         email_address VARCHAR(50),
                         street_address VARCHAR (50),
                         city VARCHAR (50),
                         province VARCHAR (50),
                         country VARCHAR (50),
                         postal_code VARCHAR (9)
);
create table if not exists employees (
                                         id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         employee_id VARCHAR(36),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email_address VARCHAR(50),
    phone_number VARCHAR(50),
    salary DECIMAL(19,4),
    commission_rate DECIMAL(3,1),
    department VARCHAR(50),
    street_address VARCHAR (50),
    city VARCHAR (50),
    province VARCHAR (50),
    country VARCHAR (50),
    postal_code VARCHAR (9)
    );
create table if not exists employees (
                                         id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         employee_id VARCHAR(36),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email_address VARCHAR(50),
    phone_number VARCHAR(50),
    salary DECIMAL(19,4),
    commission_rate DECIMAL(3,1),
    department VARCHAR(50),
    street_address VARCHAR (50),
    city VARCHAR (50),
    province VARCHAR (50),
    country VARCHAR (50),
    postal_code VARCHAR (9)
    );

create table if not exists inventories (
                                           id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                           inventory_id VARCHAR(36),
    type VARCHAR(100)
    );

create table if not exists vehicle_options (
                                               vehicle_id INTEGER,
                                               name VARCHAR(100),
    description VARCHAR(200),
    cost DECIMAL(19,2)
    );

create table if not exists vehicles (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    vin VARCHAR(17) UNIQUE,
    inventory_id VARCHAR(36),
    status VARCHAR(50),
    usage_type VARCHAR(50),
    year INTEGER,
    manufacturer VARCHAR(100),
    make VARCHAR(50),
    model VARCHAR(75),
    body_class VARCHAR(100),
    msrp DECIMAL(19,2),
    cost DECIMAL(19,2),
    total_options_cost DECIMAL(19,2)
    );