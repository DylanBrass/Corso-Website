package com.corso.springboot.FAQ_Subdomain.presentationlayer;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FAQRequest {




    public String question;

    public String answer;

    public boolean preference;

    public FAQRequest getFAQId() {
        return this;
    }
}
