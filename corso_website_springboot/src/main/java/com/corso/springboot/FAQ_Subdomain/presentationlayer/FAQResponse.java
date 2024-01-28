package com.corso.springboot.FAQ_Subdomain.presentationlayer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@Builder
public class FAQResponse {


    private String FAQId;

    private String question;

    private String answer;

    private boolean preference;
}
