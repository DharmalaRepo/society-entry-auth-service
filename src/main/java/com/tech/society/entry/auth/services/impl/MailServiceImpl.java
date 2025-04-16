package com.tech.society.entry.auth.services.impl;

import com.tech.society.entry.auth.services.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendTokenMail(String to, String tokenKey, String tokenValue, String residentName) {
        String subject = "Entry Authorization Token from " + residentName;
        String body = "Dear Visitor,\n\n"
                + "You are authorized by " + residentName + " to visit the society.\n"
                + "Please use the following token for verification:\n\n"
                + "Token: " + tokenKey + " - " + tokenValue + "\n\n"
                + "Show this token to security at the gate.\n\n"
                + "Thank you,\nRiddhis Golden Nest Society";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }


    public void sendTokenHtmlMail(String to, String tokenKey, String tokenValue, String residentName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Entry Token from " + residentName);

            String htmlContent = "<html><body>" +
                    "<h2>Visitor Entry Token</h2>" +
                    "<p><strong>Resident:</strong> " + residentName + "</p>" +
                    "<p><strong>Token:</strong> <span style='font-size:18px;color:green;'>" + tokenKey + " - " + tokenValue + "</span></p>" +
                    "<p>Please show this code to security at the gate.</p>" +
                    "<br/><p>Regards,<br/>Riddhis Golden Nest Society</p>" +
                    "</body></html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // or use a logger
        }
    }
}