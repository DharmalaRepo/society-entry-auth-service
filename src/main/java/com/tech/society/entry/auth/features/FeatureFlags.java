package com.tech.society.entry.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlags {

    @Value("${features.email.enabled:true}")
    private boolean emailEnabled;

    @Value("${features.sms.enabled:false}")
    private boolean smsEnabled;

    @Value("${features.whatsapp.enabled:false}")
    private boolean whatsappEnabled;

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }

    public boolean isWhatsappEnabled() {
        return whatsappEnabled;
    }
}