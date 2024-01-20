package com.corso.springboot.FAQ_Subdomain.presentationlayer;


import com.corso.springboot.FAQ_Subdomain.businesslayer.FAQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000","https://corsoelectriqueinc.tech/"},allowCredentials = "true")
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

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PatchMapping("/viewable")
    public ResponseEntity<List<FAQResponse>> chooseThreeFAQs(@RequestBody List<ThreeFAQsRequest> threeFAQsRequests){
        return ResponseEntity.status(HttpStatus.OK).body(faqService.chooseThreeFAQs(threeFAQsRequests));
    }

  //  @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/{faqId}")
    public ResponseEntity<FAQResponse> getFaqByFaqId(@PathVariable String faqId){
        return ResponseEntity.status(HttpStatus.OK).body(faqService.getFaqByFaqId(faqId));
    }

}
