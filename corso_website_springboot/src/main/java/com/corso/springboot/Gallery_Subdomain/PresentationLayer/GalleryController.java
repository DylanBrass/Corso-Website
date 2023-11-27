package com.corso.springboot.Gallery_Subdomain.PresentationLayer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/corso/galleries")
public class GalleryController {

    @GetMapping()
    public String test(){
        return "test";
    }
    @GetMapping("/carousel")
    public String GetCarousel(){
        return "Testing";
    }

}
