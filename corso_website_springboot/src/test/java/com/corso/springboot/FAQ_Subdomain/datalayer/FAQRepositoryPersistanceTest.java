package com.corso.springboot.FAQ_Subdomain.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


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
}

    /*
    @Test
    void getFAQSByPreferenceTrue() {
    }
}

     */