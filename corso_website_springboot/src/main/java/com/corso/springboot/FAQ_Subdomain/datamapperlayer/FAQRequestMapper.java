package com.corso.springboot.FAQ_Subdomain.datamapperlayer;


import com.corso.springboot.FAQ_Subdomain.datalayer.FAQ;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQRequest;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.ThreeFAQsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FAQRequestMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "FAQId", ignore = true),
            @Mapping(target = "question", ignore = true),
            @Mapping(target = "answer", ignore = true),
    })
    FAQ toThreeFAQsRequest(ThreeFAQsRequest threeFAQsRequest);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "FAQId", ignore = true),
    })
    FAQ toFAQ(FAQRequest faqRequest);



}
