package com.corso.springboot.FAQ_Subdomain.presentationlayer;


import com.corso.springboot.FAQ_Subdomain.businesslayer.FAQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://corsoelectriqueinc.tech/"}, allowCredentials = "true")
@Slf4j
@RestController
@RequestMapping("api/v1/corso/faqs")
public class FAQController {

    private final FAQService faqService;

    public FAQController(FAQService faqService) {
        this.faqService = faqService;
    }

    @Cacheable(value = "faqs", key = "#root.methodName")
    @GetMapping()
    public ResponseEntity<List<FAQResponse>> getFAQs() {
        return ResponseEntity.status(HttpStatus.OK).body(faqService.getFAQs());
    }

    @Cacheable(value = "faqsPreferred", key = "#root.methodName")
    @GetMapping("/preferred")
    public ResponseEntity<List<FAQResponse>> getPreferedFAQs() {
        return ResponseEntity.status(HttpStatus.OK).body(faqService.getPreferedFAQs());
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "faqs", allEntries = true),
                    @CacheEvict(value = "faqsCount", allEntries = true),
                    @CacheEvict(value = "faqsPreferred", allEntries = true)
            }
    )
    @PreAuthorize("hasAnyAuthority('Admin')")
    @PatchMapping("/viewable")
    public ResponseEntity<List<FAQResponse>> chooseThreeFAQs(@RequestBody List<ThreeFAQsRequest> threeFAQsRequests) {
        return ResponseEntity.status(HttpStatus.OK).body(faqService.chooseThreeFAQs(threeFAQsRequests));
    }


    @Cacheable(value = "faq", key = "#faqId")
    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/{faqId}")
    public ResponseEntity<FAQResponse> getFaqByFaqId(@PathVariable String faqId){
        return ResponseEntity.status(HttpStatus.OK).body(faqService.getFaqByFaqId(faqId));
    }

    @Caching(evict = {
            @CacheEvict(value = "faqs", allEntries = true),
            @CacheEvict(value = "faqsCount", allEntries = true),
            @CacheEvict(value = "faqsPreferred", allEntries = true),
            @CacheEvict(value = "faq", allEntries = true)
    })
    @PreAuthorize("hasAnyAuthority('Admin')")
    @DeleteMapping("/{faqId}")
    public ResponseEntity<FAQResponse> deleteFaqByFaqId(@PathVariable String faqId){
        return ResponseEntity.status(HttpStatus.OK).body(faqService.deleteFaqByFaqId(faqId));
    }


    @Cacheable(value = "faqsCount", key = "#root.methodName")
    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/count")
    public ResponseEntity<Integer> getFAQCount() {
        return ResponseEntity.status(HttpStatus.OK).body(faqService.getFAQCount());
    }

    @Caching(evict = {
            @CacheEvict(value = "faqs", allEntries = true),
            @CacheEvict(value = "faqsCount", allEntries = true),
            @CacheEvict(value = "faqsPreferred", allEntries = true),
            @CacheEvict(value = "faq",allEntries = true)
    })
    @PreAuthorize("hasAnyAuthority('Admin')")
    @PutMapping("/{faqId}")
    public ResponseEntity<FAQResponse> updateFaqByFaqId(@PathVariable String faqId, @RequestBody FAQRequest faqRequest){
        return ResponseEntity.status(HttpStatus.OK).body(faqService.modifyFAQ(faqId, faqRequest));
    }


    @Caching(evict = {
            @CacheEvict(value = "faqs", allEntries = true),
            @CacheEvict(value = "faqsCount", allEntries = true),
            @CacheEvict(value = "faqsPreferred", allEntries = true)

    })
    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping()
    public ResponseEntity<FAQResponse> createFAQ(@RequestBody FAQRequest faqRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.createFaq(faqRequest));
    }

}
