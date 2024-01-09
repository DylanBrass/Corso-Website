package com.corso.springboot.Review_Subdomain.datalayer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
class ReviewRepositoryPersistanceTest {


    Logger log = Logger.getLogger(ReviewRepositoryPersistanceTest.class.getName());

    @Autowired
    ReviewRepository reviewRepository;


    @Test
    void findAllByCustomerFullNameStartingWith() {
        String customerFullName = "John Doe";
        List<Review> reviews = reviewRepository.findAllByCustomerFullNameStartingWith(customerFullName);
        assertEquals(1, reviews.size());
    }





}