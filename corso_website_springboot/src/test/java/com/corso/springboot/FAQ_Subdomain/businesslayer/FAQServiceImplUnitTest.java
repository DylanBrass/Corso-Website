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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class FAQServiceImplUnitTest {


    @Mock
    private FAQRepository faqRepository;

    @Mock
    private FAQResponseMapper faqResponseMapper;

    @Mock
    private FAQRequestMapper faqRequestMapper;

    @InjectMocks
    private FAQServiceImpl faqServiceImpl;


    String FAQ_ID= "87e59aeb-3aa3-4c22-be1d-a35acde209b9";

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

    @Test
    void chooseThreeFAQs_shouldSucceed() {
        List<ThreeFAQsRequest> mockThreeFAQsRequests = new ArrayList<>();

        ThreeFAQsRequest mockThreeFAQsRequest1 = ThreeFAQsRequest.builder()
                .faqId("1")
                .preference(true)
                .build();

        ThreeFAQsRequest mockThreeFAQsRequest2 = ThreeFAQsRequest.builder()
                .faqId("2")
                .preference(true)
                .build();

        ThreeFAQsRequest mockThreeFAQsRequest3 = ThreeFAQsRequest.builder()
                .faqId("3")
                .preference(true)
                .build();

        mockThreeFAQsRequests.add(mockThreeFAQsRequest1);
        mockThreeFAQsRequests.add(mockThreeFAQsRequest2);
        mockThreeFAQsRequests.add(mockThreeFAQsRequest3);

        List<FAQ> mockUpdatedFAQs = new ArrayList<>();

        FAQ mockUpdatedFAQ1 = FAQ.builder()
                .FAQId(new FAQIdentifier())
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        FAQ mockUpdatedFAQ2 = FAQ.builder()
                .FAQId(new FAQIdentifier())
                .question("How do I contact Corso")
                .answer("You can contact us by emailing us")
                .preference(true)
                .build();

        FAQ mockUpdatedFAQ3 = FAQ.builder()
                .FAQId(new FAQIdentifier())
                .question("How do I make an order")
                .answer("By choosing the service you want and clicking the button")
                .preference(true)
                .build();

        mockUpdatedFAQs.add(mockUpdatedFAQ1);
        mockUpdatedFAQs.add(mockUpdatedFAQ2);
        mockUpdatedFAQs.add(mockUpdatedFAQ3);

        List<FAQResponse> mockUpdatedFAQResponses = new ArrayList<>();

        FAQResponse mockUpdatedFAQResponse1 = FAQResponse.builder()
                .FAQId("1")
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        FAQResponse mockUpdatedFAQResponse2 = FAQResponse.builder()
                .FAQId("2")
                .question("How do I contact Corso")
                .answer("You can contact us by emailing us")
                .preference(true)
                .build();

        FAQResponse mockUpdatedFAQResponse3 = FAQResponse.builder()
                .FAQId("3")
                .question("How do I make an order")
                .answer("By choosing the service you want and clicking the button")
                .preference(true)
                .build();

        mockUpdatedFAQResponses.add(mockUpdatedFAQResponse1);
        mockUpdatedFAQResponses.add(mockUpdatedFAQResponse2);
        mockUpdatedFAQResponses.add(mockUpdatedFAQResponse3);

        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(new FAQ("How do I create an account", "Top right corner", true));
        Mockito.when(faqRequestMapper.toThreeFAQsRequest(Mockito.any(ThreeFAQsRequest.class))).thenReturn(new FAQ("How do I create an account", "Top right corner", true));

        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(new FAQ("How do I contact Corso", "You can contact us by emailing us", true));
        Mockito.when(faqRequestMapper.toThreeFAQsRequest(Mockito.any(ThreeFAQsRequest.class))).thenReturn(new FAQ("How do I contact Corso", "You can contact us by emailing us", true));

        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(new FAQ("How do I make an order", "By choosing the service you want and clicking the button", true));
        Mockito.when(faqRequestMapper.toThreeFAQsRequest(Mockito.any(ThreeFAQsRequest.class))).thenReturn(new FAQ("How do I make an order", "By choosing the service you want and clicking the button", true));

        Mockito.when(faqRepository.saveAll(Mockito.anyList())).thenReturn(mockUpdatedFAQs);
        Mockito.when(faqResponseMapper.toFAQsResponse(Mockito.anyList())).thenReturn(mockUpdatedFAQResponses);

        // Act
        List<FAQResponse> faqResult = faqServiceImpl.chooseThreeFAQs(mockThreeFAQsRequests);

        // Assert
        assertEquals(mockUpdatedFAQResponses, faqResult);
        assertEquals(3, faqResult.size());
        assertEquals(mockUpdatedFAQs.get(0).getQuestion(), faqResult.get(0).getQuestion());
        assertEquals(mockUpdatedFAQs.get(0).getAnswer(), faqResult.get(0).getAnswer());
        assertEquals(mockUpdatedFAQs.get(1).getQuestion(), faqResult.get(1).getQuestion());
        assertEquals(mockUpdatedFAQs.get(1).getAnswer(), faqResult.get(1).getAnswer());
        assertEquals(mockUpdatedFAQs.get(2).getQuestion(), faqResult.get(2).getQuestion());
        assertEquals(mockUpdatedFAQs.get(2).getAnswer(), faqResult.get(2).getAnswer());
    }

    @Test
    void chooseThreeFAQs_shouldThrowNotEnoughPrefferedFAQsException() {
        List<ThreeFAQsRequest> mockThreeFAQsRequests = new ArrayList<>();

        ThreeFAQsRequest mockThreeFAQsRequest1 = ThreeFAQsRequest.builder()
                .faqId("1")
                .preference(true)
                .build();

        mockThreeFAQsRequests.add(mockThreeFAQsRequest1);

        List<FAQ> mockUpdatedFAQs = new ArrayList<>();

        FAQ mockUpdatedFAQ1 = FAQ.builder()
                .FAQId(new FAQIdentifier())
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        mockUpdatedFAQs.add(mockUpdatedFAQ1);

        List<FAQResponse> mockUpdatedFAQResponses = new ArrayList<>();

        FAQResponse mockUpdatedFAQResponse1 = FAQResponse.builder()
                .FAQId("1")
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        mockUpdatedFAQResponses.add(mockUpdatedFAQResponse1);

        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(new FAQ("How do I create an account", "Top right corner", true));
        Mockito.when(faqRequestMapper.toThreeFAQsRequest(Mockito.any(ThreeFAQsRequest.class))).thenReturn(new FAQ("How do I create an account", "Top right corner", true));

        Mockito.when(faqRepository.saveAll(Mockito.anyList())).thenReturn(mockUpdatedFAQs);
        Mockito.when(faqResponseMapper.toFAQsResponse(Mockito.anyList())).thenReturn(mockUpdatedFAQResponses);

        // Act
        ThreePrefferedFaqsException exception = assertThrows(ThreePrefferedFaqsException.class, () -> faqServiceImpl.chooseThreeFAQs(mockThreeFAQsRequests));

        // Assert
        assertEquals("Choose 3 FAQs to display on the main page.", exception.getMessage());
    }

    @Test
    void chooseThreeFAQs_shouldThrowThreePrefferedFaqsAlreadyExistException() {
        List<ThreeFAQsRequest> mockThreeFAQsRequests = new ArrayList<>();

        ThreeFAQsRequest mockThreeFAQsRequest1 = ThreeFAQsRequest.builder()
                .faqId("1")
                .preference(true)
                .build();

        mockThreeFAQsRequests.add(mockThreeFAQsRequest1);
        mockThreeFAQsRequests.add(mockThreeFAQsRequest1);
        mockThreeFAQsRequests.add(mockThreeFAQsRequest1);
        mockThreeFAQsRequests.add(mockThreeFAQsRequest1);

        List<FAQ> mockUpdatedFAQs = new ArrayList<>();

        FAQ mockUpdatedFAQ1 = FAQ.builder()
                .FAQId(new FAQIdentifier())
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        mockUpdatedFAQs.add(mockUpdatedFAQ1);
        mockUpdatedFAQs.add(mockUpdatedFAQ1);
        mockUpdatedFAQs.add(mockUpdatedFAQ1);
        mockUpdatedFAQs.add(mockUpdatedFAQ1);

        List<FAQResponse> mockUpdatedFAQResponses = new ArrayList<>();

        FAQResponse mockUpdatedFAQResponse1 = FAQResponse.builder()
                .FAQId("1")
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        mockUpdatedFAQResponses.add(mockUpdatedFAQResponse1);
        mockUpdatedFAQResponses.add(mockUpdatedFAQResponse1);
        mockUpdatedFAQResponses.add(mockUpdatedFAQResponse1);
        mockUpdatedFAQResponses.add(mockUpdatedFAQResponse1);

        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(new FAQ("How do I create an account", "Top right corner", true));
        Mockito.when(faqRequestMapper.toThreeFAQsRequest(Mockito.any(ThreeFAQsRequest.class))).thenReturn(new FAQ("How do I create an account", "Top right corner", true));

        Mockito.when(faqRepository.saveAll(Mockito.anyList())).thenReturn(mockUpdatedFAQs);
        Mockito.when(faqResponseMapper.toFAQsResponse(Mockito.anyList())).thenReturn(mockUpdatedFAQResponses);

        // Act
        ThreePrefferedFaqsException exception = assertThrows(ThreePrefferedFaqsException.class, () -> faqServiceImpl.chooseThreeFAQs(mockThreeFAQsRequests));

        // Assert
        assertEquals("Choose 3 FAQs to display on the main page.", exception.getMessage());

    }

    @Test
    void chooseThreeFAQs_shouldThrowFAQNotFoundException() {
        List<ThreeFAQsRequest> mockThreeFAQsRequests = new ArrayList<>();

        ThreeFAQsRequest mockThreeFAQsRequest1 = ThreeFAQsRequest.builder()
                .faqId("1")
                .preference(true)
                .build();

        mockThreeFAQsRequests.add(mockThreeFAQsRequest1);
        mockThreeFAQsRequests.add(mockThreeFAQsRequest1);
        mockThreeFAQsRequests.add(mockThreeFAQsRequest1);

        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(null);

        // Act and Assert
        FAQNotFoundException exception = assertThrows(FAQNotFoundException.class, () -> faqServiceImpl.chooseThreeFAQs(mockThreeFAQsRequests));

        // Assert
        assertEquals("FAQ not found.", exception.getMessage());
    }

    @Test
    void viewSpecificFAQ_shouldReturnFAQId(){
        FAQ mockFAQ = FAQ.builder()
                .FAQId(new FAQIdentifier())
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        FAQResponse mockFAQResponse = FAQResponse.builder()
                .FAQId("1")
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(new FAQ("How do I create an account", "Top right corner", true));
        Mockito.when(faqResponseMapper.toFAQResponse(Mockito.any(FAQ.class))).thenReturn(mockFAQResponse);

        // Act
        FAQResponse faqResult = faqServiceImpl.getFaqByFaqId(FAQ_ID);

        // Assert
        assertEquals(mockFAQResponse, faqResult);
        assertEquals(mockFAQResponse.getFAQId(), faqResult.getFAQId());
        assertEquals(mockFAQResponse.getQuestion(), faqResult.getQuestion());
        assertEquals(mockFAQResponse.getAnswer(), faqResult.getAnswer());
        assertEquals(mockFAQResponse.isPreference(), faqResult.isPreference());
    }

    @Test
    void deleteSpecificFAQ_shouldReturnFAQId(){
        FAQ mockFAQ = FAQ.builder()
                .FAQId(new FAQIdentifier())
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        FAQResponse mockFAQResponse = FAQResponse.builder()
                .FAQId("1")
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(new FAQ("How do I create an account", "Top right corner", true));
        Mockito.when(faqResponseMapper.toFAQResponse(Mockito.any(FAQ.class))).thenReturn(mockFAQResponse);

        // Act
        FAQResponse faqResult = faqServiceImpl.deleteFaqByFaqId(FAQ_ID);

        // Assert
        assertEquals(mockFAQResponse, faqResult);
        assertEquals(mockFAQResponse.getFAQId(), faqResult.getFAQId());
        assertEquals(mockFAQResponse.getQuestion(), faqResult.getQuestion());
        assertEquals(mockFAQResponse.getAnswer(), faqResult.getAnswer());
        assertEquals(mockFAQResponse.isPreference(), faqResult.isPreference());
    }

    @Test
    void deleteSpecificFAQ_shouldThrowFAQNotFoundException(){
        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(null);

        // Act and Assert
        FAQNotFoundException exception = assertThrows(FAQNotFoundException.class, () -> faqServiceImpl.deleteFaqByFaqId(FAQ_ID));

        // Assert
        assertEquals("FAQ not found.", exception.getMessage());
    }

    @Test
    void modifyFaq_shouldReturnUpdatedFaq(){

        FAQResponse mockFAQResponse = FAQResponse.builder()
                .FAQId("1")
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        FAQRequest mockFAQRequest = FAQRequest.builder()
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(false)
                .build();

        FAQ mockUpdatedFAQ = FAQ.builder()
                .FAQId(new FAQIdentifier())
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(false)
                .build();

        FAQResponse mockUpdatedFAQResponse = FAQResponse.builder()
                .FAQId("1")
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(false)
                .build();

        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(new FAQ("How do I create an account", "Top right corner", true));
        Mockito.when(faqRequestMapper.toFAQ(Mockito.any())).thenReturn(mockUpdatedFAQ);
        Mockito.when(faqRepository.save(Mockito.any(FAQ.class))).thenReturn(mockUpdatedFAQ);
        Mockito.when(faqResponseMapper.toFAQResponse(Mockito.any(FAQ.class))).thenReturn(mockUpdatedFAQResponse);

        // Act
        FAQResponse faqResult = faqServiceImpl.modifyFAQ(FAQ_ID, mockFAQRequest);

        // Assert
        assertEquals(mockUpdatedFAQResponse, faqResult);
        assertEquals(mockUpdatedFAQResponse.getFAQId(), faqResult.getFAQId());
        assertEquals(mockUpdatedFAQResponse.getQuestion(), faqResult.getQuestion());
        assertEquals(mockUpdatedFAQResponse.getAnswer(), faqResult.getAnswer());
        assertEquals(mockUpdatedFAQResponse.isPreference(), faqResult.isPreference());
    }

    @Test
    void modifyFaq_shouldThrowFAQNotFoundException(){
        FAQRequest mockFAQRequest = FAQRequest.builder()
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(false)
                .build();

        Mockito.when(faqRepository.getFAQByFAQId_FAQId(Mockito.anyString())).thenReturn(null);

        // Act and Assert
        FAQNotFoundException exception = assertThrows(FAQNotFoundException.class, () -> faqServiceImpl.modifyFAQ(FAQ_ID, mockFAQRequest));

        // Assert
        assertEquals("FAQ not found.", exception.getMessage());
    }


    @Test
    void getFAQCount_shouldReturnCount(){
        Mockito.when(faqRepository.countFAQBy()).thenReturn(5);

        // Act
        Integer faqResult = faqServiceImpl.getFAQCount();

        // Assert
        assertEquals(5, faqResult);
    }

    @Test
    void createFAQ_shouldReturnFAQId(){
        FAQ mockFAQ = FAQ.builder()
                .FAQId(new FAQIdentifier())
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        FAQResponse mockFAQResponse = FAQResponse.builder()
                .FAQId("1")
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        FAQRequest mockFAQRequest = FAQRequest.builder()
                .question("How do I create an account")
                .answer("Top right corner")
                .preference(true)
                .build();

        Mockito.when(faqRequestMapper.toFAQ(Mockito.any(com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQRequest.class))).thenReturn(new FAQ("How do I create an account", "Top right corner", true));
        Mockito.when(faqRepository.save(Mockito.any(FAQ.class))).thenReturn(mockFAQ);
        Mockito.when(faqResponseMapper.toFAQResponse(Mockito.any(FAQ.class))).thenReturn(mockFAQResponse);

        // Act
        FAQResponse faqResult = faqServiceImpl.createFaq(mockFAQRequest);

        // Assert
        assertEquals(mockFAQResponse, faqResult);
        assertEquals(mockFAQResponse.getFAQId(), faqResult.getFAQId());
        assertEquals(mockFAQResponse.getQuestion(), faqResult.getQuestion());
        assertEquals(mockFAQResponse.getAnswer(), faqResult.getAnswer());
        assertEquals(mockFAQResponse.isPreference(), faqResult.isPreference());
    }

}