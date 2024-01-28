package com.corso.springboot.Customer_Subdomain.datalayer;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "customers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Customer {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String userId;

        private String name;

        private String email;

        private String phone;

        private String address;

        private String postalCode;

        private String city;

        private String apartmentNumber;

        private boolean verified;

}
