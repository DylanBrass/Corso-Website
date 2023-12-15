package com.corso.springboot.FAQ_Subdomain.datalayer;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FAQIdentifier {

    @Column(name = "faq_id")
    private String FAQId;

    public FAQIdentifier() {
        this.FAQId = java.util.UUID.randomUUID().toString();
    }

    public String getFAQId() {
        return FAQId;
    }
}
