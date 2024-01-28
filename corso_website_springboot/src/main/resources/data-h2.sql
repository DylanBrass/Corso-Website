-- Insert dummy data into `gallery` table
INSERT INTO `gallery` (gallery_id, description, photo, image_position)
VALUES ('e8da0dc7-eecc-47af-831e-ae6d5101cc24', 'A example of work for a test', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701099346/test-image-carousel-1_xd1nwu.jpg', 1);
INSERT INTO `gallery` (gallery_id, description, photo, image_position)
VALUES ('ed948a48-bced-404e-bc55-6f983c1f304b', 'A example of work for a test', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701099722/test-image-carousel-2_ah3zfx.png', 2);
INSERT INTO `gallery` (gallery_id, description, photo, image_position)
VALUES ('c3711305-3e95-4dce-aec5-c835cb662225', 'A example of work for a test', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701099722/test-image-carousel-3_gh3fem.png', 3);
INSERT INTO `gallery` (gallery_id, description, photo, image_position)
VALUES ('cae88af2-8dd2-4087-bcd4-d863f60dda47', 'A example of work for a test', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701099722/test-image-carousel-4_hyt2w5.jpg', 4);
INSERT INTO `gallery` (gallery_id, description, photo, image_position)
VALUES ('62b068a9-ef01-4bfd-8ee4-437deb43c3f6', 'A example of work for a test', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg', 5);

-- Insert dummy data into `services` table
INSERT INTO `services` (service_id, service_name, service_description, service_icon, service_image, is_active)
VALUES
    ('982577e6-1909-46b8-8583-e08c9daa4e9b', 'Electrical Repairs', 'Professional electrical repair services', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg',1),
    ('b581b538-d6d5-44f1-b288-b45824e4dd4c', 'Lighting Installation', 'Expert lighting installation for homes and businesses', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg',1),
    ('14e10861-5472-4e33-bb7b-b6e7ba2a83f6', 'Wiring Services', 'Quality electrical wiring services for new constructions', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg',1),
    ('7bf6a544-b34d-478a-898a-a64603100fcf', 'Appliance Repair', 'Swift and reliable appliance repair solutions', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg',1),
    ('953a8b11-3c0a-4a9b-900d-2ab3395d5d81', 'Emergency Services', '24/7 emergency electrical services', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png', 'https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg',1);

-- Insert dummy data into `orders` table
INSERT INTO `orders` ( order_id, service_id, user_id,progress_information, order_tracking_number, order_date, due_date, order_status, order_description, customer_full_name, customer_email, customer_phone, customer_address, customer_postal_code, customer_city, customer_apartment_number, hours_worked, estimated_duration) VALUES ( 'ac5ca2b4-d53c-4516-8d4b-17665b46f411', '982577e6-1909-46b8-8583-e08c9daa4e9b', 'auth0|65702e81e9661e14ab3aac89','test', 'T1YX4S', '2023-01-01', '2023-02-01', 'IN_PROGRESS', 'Custom web development project', 'John Doe', 'john@example.com', '1234567890', '123 Main St', 'J5R 7J4', 'Sait-Laurent, MTL', '34', 6, 8);
INSERT INTO `orders` ( order_id, service_id, user_id, progress_information,order_tracking_number, order_date, due_date, order_status, order_description, customer_full_name, customer_email, customer_phone, customer_address, customer_postal_code, customer_city, customer_apartment_number, hours_worked, estimated_duration) VALUES ( '10f906fe-e014-453d-ab83-4ea523646e1e', 'b581b538-d6d5-44f1-b288-b45824e4dd4c', 'auth0|65702e81e9661e14ab3aac89', 'test','J0ZW6D', '2023-02-01', '2023-03-01', 'COMPLETED', 'Logo design for new brand', 'Jane Smith', 'jane@example.com', '9876543210', '456 Oak St', 'J6R 7J4', 'Brossard', null, 0, 5);
INSERT INTO `orders` ( order_id, service_id, user_id,progress_information, order_tracking_number, order_date, due_date, order_status, order_description, customer_full_name, customer_email, customer_phone, customer_address, customer_postal_code, customer_city, customer_apartment_number, hours_worked, estimated_duration) VALUES ( 'a0119367-4963-4019-b433-ba639fdf6b41', '14e10861-5472-4e33-bb7b-b6e7ba2a83f6', 'auth0|65702e81e9661e14ab3aac89', 'test','P4G87V', '2023-03-01', null, 'PENDING', 'Social media marketing campaign', 'Bob Johnson', 'bob@example.com', null, '789 Pine St', 'J4R 8J8', 'Rimouski', null, 3, 3);
INSERT INTO `orders` ( order_id, service_id, user_id, progress_information,order_tracking_number, order_date, due_date, order_status, order_description, customer_full_name, customer_email, customer_phone, customer_address, customer_postal_code, customer_city, customer_apartment_number, hours_worked, estimated_duration) VALUES ( '1ab0b0bb-9a49-44d3-a171-188a303ebc8d', '7bf6a544-b34d-478a-898a-a64603100fcf', 'auth0|65702e81e9661e14ab3aac89', 'test','LRM3KT', '2023-04-01', '2023-05-01', 'CANCELLED', 'iOS app development', 'Alice Williams', 'alice@example.com', '5432109876', '101 Elm St', 'J3R 7J9', 'Quebec City', '3', 0, 5);
INSERT INTO `orders` ( order_id, service_id, user_id,progress_information, order_tracking_number, order_date, due_date, order_status, order_description, customer_full_name, customer_email, customer_phone, customer_address, customer_postal_code, customer_city, customer_apartment_number, hours_worked, estimated_duration) VALUES ( '0a44b38b-08af-4321-8117-9a5c32ff3a29', '953a8b11-3c0a-4a9b-900d-2ab3395d5d81', 'auth0|65702e81e9661e14ab3aac89', 'test','1E92BF', '2023-05-01', '2023-06-01', 'COMPLETED', 'SEO optimization for website', 'David Brown', 'david@example.com', '9876123450', '202 Maple St', 'J4R 4J4', 'Saint-Jean', null, 0, 8);

-- Insert dummy data into `FAQs` table
INSERT INTO `faqs` (faq_id, question, answer, preference)
VALUES
    ('87e59aeb-3aa3-4c22-be1d-a35acde209b9', 'What services do you offer?', 'We offer a range of services including web development, graphic design, digital marketing, mobile app development, and SEO optimization.', true),
    ('368918a2-dbfe-4b50-bc6b-9ee3a7c5a0eb', 'How can I place an order?', 'You can place an order by visiting our website and selecting the desired service. Follow the instructions to complete the order process.', false),
    ('5b1ae831-a5cb-4d42-b3f3-af11b87b0f6b', 'What is the typical turnaround time for orders?', 'The turnaround time depends on the complexity of the project. We provide estimated delivery dates during the ordering process.', false),
    ('30c0e531-5679-40a6-aaf1-13e3e052230f', 'Can I request customizations to my order?', 'Yes, you can discuss customizations with our team during the order process. We strive to accommodate your specific requirements.', true),
    ('753a87e5-201e-4d4d-bd67-8393bc9b88f7', 'How can I contact customer support?', 'You can contact our customer support team through the contact form on our website or by emailing support@example.com.', true);

-- Insert dummy data into `reviews` table
INSERT INTO `reviews` (review_id, user_id, customer_full_name, review_date, message, review_rating, pinned)
VALUES
    ('e2d2cb36-5a3e-4180-afdd-b416b66a4546', 'auth0|65702e81e9661e14ab3aac89', 'John Doe', '2023-02-15', 'Excellent service and communication throughout the project!', 5, true),
    ('bb8a5754-c79b-4156-802d-704b90da3320', 'auth0|65702e81e9661e14ab3aac89', 'Jane Smith', '2023-03-15', 'The graphic design work exceeded my expectations. Very satisfied!', 4, false),
    ('8987c71a-861b-496c-a927-0683816cd490', 'auth0|65702e81e9661e14ab3aac89', 'Bob Johnson', '2023-04-15', 'Looking forward to seeing the results of the digital marketing campaign.', 3, false),
    ('874ba5d3-f917-4b84-bde6-da3d666144ad', 'auth0|65702e81e9661e14ab3aac89', 'Alice Williams', '2023-05-15', 'Impressed with the progress of the iOS app development. Great team!', 5, true),
    ('7dcca21e-4d7b-4e8c-8c58-1a044acba519', 'auth0|65702e81e9661e14ab3aac89', 'David Brown', '2023-06-15', 'Noticed a significant improvement in website traffic after SEO optimization. Thank you!', 5, false);

-- Inserting a customer named Dylan Brassard
INSERT INTO `customers` (id, user_id, name, email, phone, address, postal_code, apartment_number, city, verified)
VALUES (16, 'facebook|311881271807762', 'Dylan Brassard', 'dylan.brassard@outlook.com', '', '3333 Street', 'J5R 5J4', '', 'City',true);

-- Inserting a customer named Alice Doe
INSERT INTO `customers` (id, user_id, name, email, phone, address, postal_code, apartment_number, city, verified)
VALUES (17, 'google|123456789', 'Alice Doe', 'alice.doe@gmail.com', '+1234567890', '123 Main St', 'A1B 2C3', 'Apt 5', 'Cityville', true);

-- Inserting a customer named John Smith
INSERT INTO `customers` (id, user_id, name, email, phone, address, postal_code, apartment_number, city, verified)
VALUES (18, 'apple|987654321', 'John Smith', 'john.smith@yahoo.com', '+9876543210', '456 Oak St', 'X0X 1Y2', '', 'Towndale', true);

-- Inserting a customer named Emma Johnson
INSERT INTO `customers` (id, user_id, name, email, phone, address, postal_code, apartment_number, city, verified)
VALUES (19, 'auth0|456789012', 'Emma Johnson', 'emma.johnson@hotmail.com', '+1122334455', '789 Pine St', 'M1N 2P3', 'Apt 10', 'Metrocity', true);

-- Inserting a customer named Michael Brown
INSERT INTO `customers` (id, user_id, name, email, phone, address, postal_code, apartment_number, city, verified)
VALUES (20, 'auth0|9876543210', 'Michael Brown', 'michael.brown@gmail.com', '+9876543210', '101 Elm St', 'Z9Z 8X7', '', 'Greenville', true);

-- Inserting a customer named Sophia Lee
INSERT INTO `customers` (id, user_id, name, email, phone, address, postal_code, apartment_number, city, verified)
VALUES (21, 'auth0|6543210987', 'Sophia Lee', 'sophia.lee@outlook.com', '+1122334455', '555 Maple St', 'K4K 7L6', 'Apt 3', 'Techtown', false);
