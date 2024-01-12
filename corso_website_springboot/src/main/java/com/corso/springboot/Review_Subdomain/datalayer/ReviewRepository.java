package com.corso.springboot.Review_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {


    List<Review> getReviewByPinned(boolean pinned);

    @Query("SELECT r FROM Review r WHERE r.customerFullName LIKE ?1%")
    List<Review> findAllByCustomerFullNameStartingWith(String customerFullName);

    List<Review> findAllByUserId(String userId);

}
