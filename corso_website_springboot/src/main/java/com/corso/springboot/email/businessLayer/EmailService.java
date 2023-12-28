package com.corso.springboot.email.businessLayer;

import javax.mail.MessagingException;

public interface EmailService {

    int sendEmail(String recipient, String subject, String text) throws MessagingException;

}
