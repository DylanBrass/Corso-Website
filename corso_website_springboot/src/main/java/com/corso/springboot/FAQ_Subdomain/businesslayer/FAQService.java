package com.corso.springboot.FAQ_Subdomain.businesslayer;

import com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQRequest;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQResponse;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.ThreeFAQsRequest;

import java.util.List;

public interface FAQService {

    List<FAQResponse> getPreferedFAQs();

    List<FAQResponse> getFAQs();

    List<FAQResponse> chooseThreeFAQs(List<ThreeFAQsRequest> threeFAQsRequest);

    FAQResponse getFaqByFaqId(String faqId);

    FAQResponse deleteFaqByFaqId(String faqId);

    Integer getFAQCount();

    FAQResponse modifyFAQ(String faqId, FAQRequest faqRequest);


//    FAQResponse createFAQ(FAQRequest faqRequest);

    FAQResponse createFaq(FAQRequest faqRequest);

}
