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

    public FAQ(String question, String answer, boolean preference) {
        this.FAQId = new FAQIdentifier();
        this.question = question;
        this.answer = answer;
        this.preference = preference;
    }

}
