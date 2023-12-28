package com.corso.springboot.email.businessLayer;

import lombok.Generated;

import javax.mail.MessagingException;

@Generated
public interface EmailService {

    int sendEmail(String recipient, String subject, String text) throws MessagingException;

}
