package com.corso.springboot.Customer_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {

        Customer getCustomerByUserId(String userId);

        void deleteCustomerByUserId(String userId);

        boolean existsByUserId(String userId);
}
