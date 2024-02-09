package com.corso.springboot.email.businessLayer;

import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@Generated
public class EmailServiceImpl implements EmailService {

    private final String username;
    private final TemplateEngine templateEngine;

    Session session;

    @Generated
    public EmailServiceImpl(@Value("${spring.mail.username}") String username,
                            @Value("${spring.mail.password}") String password, TemplateEngine templateEngine) {


        this.username = username;
        this.templateEngine = templateEngine;

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        session = javax.mail.Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Generated
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(username, password);
                    }
                });
    }

    @Override
    public int sendEmail(String recipient, String subject, String text) throws MessagingException {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient)
            );
            message.setSubject(subject);

            message.setText(text);

            //error is handled by the global controller handler
            Transport.send(message);
            log.info("Email sent");
            //if no error is thrown, return 200
            return HttpStatus.SC_OK;
        } catch (MessagingException e) {
            //if an error is thrown, return 500
            throw new MessagingException();
        } catch (Exception e) {
            //if an error is thrown, return 500
            log.error(e.getMessage());
            return HttpStatus.SC_UNPROCESSABLE_ENTITY;
        }
    }

    @Override
    public int sendEmail(String recipient, String subject, String template, Map<String, String> parameters) throws MessagingException {
        try {
            log.info("Sending email to {}", recipient);
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                log.info("Parameter {} : {}", entry.getKey(), entry.getValue());
            }
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient)
            );
            message.setSubject(subject);

            Context context = new Context();
            //loop for all parameters and add them to the context

            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }

            String processedString = templateEngine.process(template, context);

            //error is handled by the global controller handler

            message.setContent(processedString, "text/html; charset=utf-8");


            Transport.send(message);
            return HttpStatus.SC_OK;
        } catch (MessagingException e) {
            throw new MessagingException();
        } catch (Exception e) {
            log.error(e.getMessage());
            return HttpStatus.SC_UNPROCESSABLE_ENTITY;
        }
    }



}

