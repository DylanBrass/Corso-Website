package com.corso.springboot.Review_Subdomain.datalayer;

import com.corso.springboot.Customer_Subdomain.datalayer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {

    @Query("SELECT r FROM Review r WHERE r.customerFullName LIKE ?1%")
    List<Review> findAllByCustomerFullNameStartingWith(String customerFullName);



}
