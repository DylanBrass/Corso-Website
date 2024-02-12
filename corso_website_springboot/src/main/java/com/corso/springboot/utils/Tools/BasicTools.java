package com.corso.springboot.utils.Tools;

import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.corso.springboot.email.businessLayer.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class BasicTools {

    @Value("${frontend.url}")
    private String frontendDomain;

    public final Auth0ManagementService auth0ManagementService;

    public final EmailService emailService;



    public String getFormattedDomain() {
        String url = frontendDomain.replace("https://", "").replace("http://", "")
                .split(":")[0].replace("/", "");

        log.debug("Formatted domain: " + url);

        return url;
    }

    public void sendEmailToAdmins(String subject, String template, Map<String, String> parameters) throws MessagingException {
        List<UserInfoResponse> admins = auth0ManagementService.getAllAdmins();

        StringBuilder recipients = new StringBuilder();

        for (UserInfoResponse admin : admins) {
            recipients.append(admin.getEmail()).append(",");
        }

        if (recipients.length() > 0) {
            log.info("Sending email to {}", recipients);

            recipients.deleteCharAt(recipients.length() - 1);

            int status = emailService.sendEmail(recipients.toString(), subject, template, parameters);

            if (status != 200) {
                throw new MessagingException("Failed to send email");
            }
        } else {
            log.warn("No admin recipients found. Email not sent.");
        }
    }

}

