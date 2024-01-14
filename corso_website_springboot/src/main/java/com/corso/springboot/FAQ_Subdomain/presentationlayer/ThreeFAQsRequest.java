package com.corso.springboot.FAQ_Subdomain.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreeFAQsRequest {
    public String faqId;
    public boolean preference;
}