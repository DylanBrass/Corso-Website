package com.corso.springboot.FAQ_Subdomain.presentationlayer;


import com.corso.springboot.FAQ_Subdomain.businesslayer.FAQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
@Slf4j
@RestController
@RequestMapping("api/v1/corso/faqs")
public class FAQController {

    private final FAQService faqService;

    public FAQController(FAQService faqService) {
        this.faqService = faqService;
    }
    @GetMapping()
    public ResponseEntity<List<FAQResponse>> getFAQs(){
        return ResponseEntity.status(HttpStatus.OK).body(faqService.getFAQs());
    }

    @GetMapping("/preferred")
    public ResponseEntity<List<FAQResponse>> getPreferedFAQs(){
        return ResponseEntity.status(HttpStatus.OK).body(faqService.getPreferedFAQs());
    }




}
