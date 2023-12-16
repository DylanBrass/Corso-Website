package com.corso.springboot.FAQ_Subdomain.businesslayer;


import com.corso.springboot.FAQ_Subdomain.datalayer.FAQRepository;
import com.corso.springboot.FAQ_Subdomain.datamapperlayer.FAQResponseMapper;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService{

    private final FAQRepository faqRepository;

    private final FAQResponseMapper faqResponseMapper;

    @Override
    public List<FAQResponse> getPreferedFAQs() {
        return faqResponseMapper.toFAQsResponse(faqRepository.getFAQSByPreferenceTrue());
    }

    @Override
    public List<FAQResponse> getFAQs() {
        return faqResponseMapper.toFAQsResponse(faqRepository.findAll());
    }
}
