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

@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
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

    @PostMapping("/orderAccepted")
    public ResponseEntity<Void> sendOrderAcceptedEmail(@RequestBody EmailRequest emailRequest) throws MessagingException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("H1", "Thank you for choosing Corso.");
        parameters.put("H2", "Your order has been accepted.");
        parameters.put("H4", "For more information about your order, progress reports or to leave a review, " +
                "please log in and navigate to the current orders page.");
        parameters.put("H5", "We look forward to working with you.");
        int status = emailService.sendEmail(emailRequest.getRecipient(), emailRequest.getSubject(), "ApproveOrder", parameters);

        if (status == 200)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(status).build();
    }

}
