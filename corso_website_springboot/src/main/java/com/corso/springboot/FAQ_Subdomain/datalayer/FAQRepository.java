package com.corso.springboot.FAQ_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FAQRepository extends JpaRepository<FAQ, String> {

    FAQ getFAQSByFAQId_FAQId(String FAQId);


    List<FAQ> getFAQSByPreferenceTrue();
}

