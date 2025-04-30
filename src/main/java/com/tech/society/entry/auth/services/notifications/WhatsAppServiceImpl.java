package com.tech.society.entry.auth.services.notifications;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppServiceImpl implements WhatsAppService {

    @Value("${features.whatsapp.enabled:false}")
    private boolean whatsappEnabled;

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.whatsappNumber}")
    private String whatsappNumber;


    @Override
    public void sendOtp(String toPhone, String otp, String visitorName, String flatInfo) {
        if (!whatsappEnabled) return;

        String messageBody = String.format(
                "Hello %s, your OTP for entry to %s is: %s", visitorName, flatInfo, otp);

        Message.creator(
                new PhoneNumber("whatsapp:" + toPhone),
                new PhoneNumber(whatsappNumber),
                messageBody
        ).create();
    }
}