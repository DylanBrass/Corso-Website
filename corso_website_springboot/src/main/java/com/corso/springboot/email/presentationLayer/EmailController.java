package com.corso.springboot.email.presentationLayer;

import com.corso.springboot.email.businessLayer.EmailService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000","https://corso-dev-frontend-xtejdm3liq-nn.a.run.app/"},allowCredentials = "true")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/corso/email")
@Generated
public class EmailController {


    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest emailRequest) throws MessagingException {

        int status = emailService.sendEmail(emailRequest.getRecipient(), emailRequest.getSubject(), emailRequest.getMessage());

        if (status == 200)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(status).build();
    }


    @PostMapping("/template")
    public ResponseEntity<Void> sendEmailWithTemplate() throws MessagingException {

        Map<String, String> parameters = new HashMap<>();

        parameters.put("message", "testing pls work");

        parameters.put("cool", "Dylan");

        int status = emailService.sendEmail("dylan.brassard@outlook.com", "emailRequest.getSubject()", "test", parameters);

        if (status == 200)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(status).build();
    }


}
