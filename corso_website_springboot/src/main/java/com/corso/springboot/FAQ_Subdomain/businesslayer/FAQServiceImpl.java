package com.corso.springboot.FAQ_Subdomain.businesslayer;


import com.corso.springboot.FAQ_Subdomain.datalayer.FAQ;
import com.corso.springboot.FAQ_Subdomain.datalayer.FAQIdentifier;
import com.corso.springboot.FAQ_Subdomain.datalayer.FAQRepository;
import com.corso.springboot.FAQ_Subdomain.datamapperlayer.FAQRequestMapper;
import com.corso.springboot.FAQ_Subdomain.datamapperlayer.FAQResponseMapper;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQRequest;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQResponse;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.ThreeFAQsRequest;
import com.corso.springboot.utils.exceptions.FAQ.FAQNotFoundException;
import com.corso.springboot.utils.exceptions.FAQ.ThreePrefferedFaqsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService{

    private final FAQRepository faqRepository;

    private final FAQResponseMapper faqResponseMapper;

    private final FAQRequestMapper faqRequestMapper;

    @Override
    public List<FAQResponse> getPreferedFAQs() {
        return faqResponseMapper.toFAQsResponse(faqRepository.getFAQSByPreferenceTrue());
    }

    @Override
    public List<FAQResponse> getFAQs() {
        return faqResponseMapper.toFAQsResponse(faqRepository.findAll());
    }

    @Override
    public List<FAQResponse> chooseThreeFAQs(List<ThreeFAQsRequest> threeFAQsRequests) {
        List<FAQ> updatedFAQs = new ArrayList<>();

        int numberOfPrefferedFAQs = 0;

        for (ThreeFAQsRequest threeFAQsRequest : threeFAQsRequests) {
            if (threeFAQsRequest.isPreference()) {
                numberOfPrefferedFAQs++;
            }
        }

        if (numberOfPrefferedFAQs != 3) {
            throw new ThreePrefferedFaqsException("Choose 3 FAQs to display on the main page.");
        }

        for (ThreeFAQsRequest threeFAQsRequest : threeFAQsRequests) {
            FAQ faqToUpdate = faqRepository.getFAQByFAQId_FAQId(threeFAQsRequest.getFaqId());
            if (faqToUpdate == null) {
                throw new FAQNotFoundException("FAQ not found.");
            }

            FAQ updatedFAQ = faqRequestMapper.toThreeFAQsRequest(threeFAQsRequest);
            updatedFAQ.setId(faqToUpdate.getId());
            updatedFAQ.setFAQId(faqToUpdate.getFAQId());
            updatedFAQ.setQuestion(faqToUpdate.getQuestion());
            updatedFAQ.setAnswer(faqToUpdate.getAnswer());
            updatedFAQ.setPreference(threeFAQsRequest.isPreference());
            updatedFAQs.add(updatedFAQ);
        }

        return faqResponseMapper.toFAQsResponse(faqRepository.saveAll(updatedFAQs));
    }

    @Override
    public FAQResponse getFaqByFaqId(String faqId) {
        return faqResponseMapper.toFAQResponse(faqRepository.getFAQByFAQId_FAQId(faqId));
    }

    @Override
    public FAQResponse deleteFaqByFaqId(String faqId) {
        FAQ faqToDelete = faqRepository.getFAQByFAQId_FAQId(faqId);
        if (faqToDelete == null) {
            throw new FAQNotFoundException("FAQ not found.");
        }
        faqRepository.delete(faqToDelete);
        return faqResponseMapper.toFAQResponse(faqToDelete);
    }

    @Override
    public Integer getFAQCount() {
        return faqRepository.countFAQBy();
    }

    @Override
    public FAQResponse modifyFAQ(String faqId, FAQRequest faqRequest) {
        FAQ faqToUpdate = faqRepository.getFAQByFAQId_FAQId(faqId);

        if (faqToUpdate == null) {
            throw new FAQNotFoundException("FAQ not found.");
        }

        faqToUpdate.setQuestion(faqRequest.getQuestion());
        faqToUpdate.setAnswer(faqRequest.getAnswer());
        faqToUpdate.setPreference(faqRequest.isPreference());

        FAQ updatedFAQ = faqRepository.save(faqToUpdate);

        return faqResponseMapper.toFAQResponse(updatedFAQ);
    }


    @Override
    public FAQResponse createFaq(FAQRequest faqRequest) {
        FAQ faq = faqRequestMapper.toFAQ(faqRequest);
        faq.setFAQId(new FAQIdentifier());
        return faqResponseMapper.toFAQResponse(faqRepository.save(faq));
    }



}