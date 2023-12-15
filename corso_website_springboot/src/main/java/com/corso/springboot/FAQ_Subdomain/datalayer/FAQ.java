package com.corso.springboot.FAQ_Subdomain.datalayer;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@Table(name = "FAQs")
@AllArgsConstructor
@Entity
public class FAQ{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public FAQIdentifier FAQId;
    public String question;

    public String answer;

    public boolean preference;

<<<<<<< Updated upstream
=======


>>>>>>> Stashed changes
}
