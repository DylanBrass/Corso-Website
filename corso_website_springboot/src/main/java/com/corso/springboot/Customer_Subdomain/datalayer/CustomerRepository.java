package com.corso.springboot.Customer_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    Customer getCustomerByUserId(String userId);

    void deleteCustomerByUserId(String userId);

    boolean existsByUserId(String userId);


    List<Customer> findAllByNameStartingWithAndVerified(String name,boolean isVerified);

    List<Customer> findAllByEmailStartingWithAndVerified(String email, boolean isVerified);

    Customer getCustomerByEmailAndUserId(String email, String userId);

}
