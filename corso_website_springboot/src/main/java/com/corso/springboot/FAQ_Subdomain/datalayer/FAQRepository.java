package com.corso.springboot.FAQ_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FAQRepository extends JpaRepository<FAQ, String> {
    FAQ getFAQByFAQId_FAQId(String faqId);

    List<FAQ> getFAQSByPreferenceTrue();

    Integer countFAQBy();



}

