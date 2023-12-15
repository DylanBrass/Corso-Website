package com.corso.springboot.FAQ_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FAQRepository extends JpaRepository<FAQ, String> {

<<<<<<< Updated upstream
  //  FAQ getFAQSByFAQId_FAQId(String FAQId);
=======
    FAQ getFAQSByFAQId_FAQId(String FAQId);
>>>>>>> Stashed changes


    List<FAQ> getFAQSByPreferenceTrue();
}

