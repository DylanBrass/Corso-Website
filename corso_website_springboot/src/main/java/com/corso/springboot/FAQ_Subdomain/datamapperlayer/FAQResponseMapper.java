package com.corso.springboot.FAQ_Subdomain.datamapperlayer;


import com.corso.springboot.FAQ_Subdomain.datalayer.FAQ;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQResponse;
import com.corso.springboot.Gallery_Subdomain.datalayer.Gallery;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface FAQResponseMapper {

    @Mapping(expression="java(faq.getFAQId().getFAQId())", target = "FAQId")
    FAQResponse toFAQResponse(FAQ faq);
    List<FAQResponse> toFAQsResponse(List<FAQ> faqs);

}
