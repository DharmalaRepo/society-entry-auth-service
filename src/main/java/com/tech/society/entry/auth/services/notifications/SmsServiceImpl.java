package com.tech.society.entry.auth.services.notifications;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${features.sms.enabled:false}")
    private boolean smsEnabled;

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.fromNumber}")
    private String fromNumber;

    @Override
    public void sendOtp(String toPhone, String otp, String visitorName, String flatInfo) {
        if (!smsEnabled) return;

        String messageBody = String.format(
                "Hello %s, your OTP for entry to %s is: %s", visitorName, flatInfo, otp);

        Message.creator(
                new PhoneNumber(toPhone),
                new PhoneNumber(fromNumber),
                messageBody
        ).create();
    }
}
