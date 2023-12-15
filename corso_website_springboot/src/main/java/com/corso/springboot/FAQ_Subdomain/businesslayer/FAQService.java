package com.corso.springboot.FAQ_Subdomain.businesslayer;

import com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQResponse;

import java.util.List;

public interface FAQService {

    List<FAQResponse> getPreferedFAQs();

    List<FAQResponse> getFAQs();

}
