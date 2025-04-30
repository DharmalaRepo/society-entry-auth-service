package com.tech.society.entry.auth.services;

public interface MailService {
    void sendTokenMail(String to, String tokenKey, String tokenValue, String residentName);
    void sendTokenHtmlMail(String to, String tokenKey, String tokenValue, String residentName);

    void sendOtpToVisitor(String toEmail, String otp, String visitorName, String flatInfo);
}