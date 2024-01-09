package com.corso.springboot.FAQ_Subdomain.businesslayer;

import com.corso.springboot.FAQ_Subdomain.datalayer.FAQ;
import com.corso.springboot.FAQ_Subdomain.datalayer.FAQIdentifier;
import com.corso.springboot.FAQ_Subdomain.datalayer.FAQRepository;
import com.corso.springboot.FAQ_Subdomain.datamapperlayer.FAQResponseMapper;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class FAQServiceImplUnitTest {


    @Mock
    private FAQRepository faqRepository;

    @Mock
    private FAQResponseMapper faqResponseMapper;

    @InjectMocks
    private FAQServiceImpl faqServiceImpl;

    @Test
    void getPreferedFAQs() {
        List<FAQResponse> mockFAQResponses = Arrays.asList(
                new FAQResponse("1", "How do I create an account", "Top right corner", true),
                new FAQResponse("2", "How do I contact Corso", "You can contact us by emailing us", true)
        );

        FAQ faq1 = FAQ.builder()
                .id(1)
                .FAQId(new FAQIdentifier())
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        FAQ faq2 = FAQ.builder()
                .id(2)
                .FAQId(new FAQIdentifier())
                .question("How do I contact Corso")
                .answer("You can contact us by emailing us")
                .preference(true)
                .build();


        Mockito.when(faqRepository.getFAQSByPreferenceTrue()).thenReturn(Arrays.asList(faq1, faq2));

        Mockito.when(faqResponseMapper.toFAQsResponse(Mockito.anyList())).thenReturn(mockFAQResponses);

    }

    @Test
    void getFAQs() {

        List<FAQResponse> mockFAQResponses = Arrays.asList(
                new FAQResponse("1", "How do I create an account", "Top right corner", true),
                new FAQResponse("2", "How do I contact Corso", "You can contact us by emailing us", false)
        );

        FAQ faq1 = FAQ.builder()
                .id(1)
                .FAQId(new FAQIdentifier())
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        FAQ faq2 = FAQ.builder()
                .id(2)
                .FAQId(new FAQIdentifier())
                .question("How do I contact Corso")
                .answer("You can contact us by emailing us")
                .preference(false)
                .build();


        Mockito.when(faqRepository.findAll()).thenReturn(Arrays.asList(faq1, faq2
        ));

        Mockito.when(faqResponseMapper.toFAQsResponse(Mockito.anyList())).thenReturn(mockFAQResponses);

        // Act
        List<FAQResponse> faqResult = faqServiceImpl.getFAQs();

        // Assert
        assertEquals(mockFAQResponses, faqResult);
        Mockito.verify(faqRepository, Mockito.times(1)).findAll();
        Mockito.verify(faqResponseMapper, Mockito.times(1)).toFAQsResponse(Mockito.anyList());
    }
}