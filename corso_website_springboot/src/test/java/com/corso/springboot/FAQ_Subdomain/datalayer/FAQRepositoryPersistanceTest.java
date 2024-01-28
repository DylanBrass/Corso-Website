package com.corso.springboot.FAQ_Subdomain.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertNotNull;


@DataJpaTest
@ActiveProfiles("test")
class FAQRepositoryPersistanceTest {
    @Autowired
    FAQRepository faqRepository;
    private FAQ preSavedfaq;

    @BeforeEach
    public void setup() {
        faqRepository.deleteAll();
        preSavedfaq = faqRepository.save(
                new FAQ(1, new FAQIdentifier(), "How do I create an account", "Top right corner", true));


    }

    @Test
    public void testGetFAQByFAQId_FAQId() {
        FAQ faq = faqRepository.getFAQByFAQId_FAQId(preSavedfaq.getFAQId().getFAQId());
        assert(faq.getFAQId().getFAQId().equals(preSavedfaq.getFAQId().getFAQId()));
    }

    @Test
    public void testGetFAQCount() {
        assert(faqRepository.count() == 1);
    }

    @Test
    public void testCreateFAQ() {
        FAQ faq = faqRepository.save(new FAQ(1, new FAQIdentifier(), "How do I create an account", "Top right corner", false));

        // Assert
        assertNotNull(faq.getFAQId().getFAQId());
    }

}