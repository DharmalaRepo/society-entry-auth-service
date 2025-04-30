package com.tech.society.entry.auth.services.notifications;

public interface SmsService {
    void sendOtp(String toPhone, String otp, String visitorName, String flatInfo);
}