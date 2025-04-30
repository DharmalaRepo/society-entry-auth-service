package com.tech.society.entry.auth.dto;

public class OtpVerificationRequestDTO {
    private String otp;
    private String entryGate;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEntryGate() {
        return entryGate;
    }

    public void setEntryGate(String entryGate) {
        this.entryGate = entryGate;
    }
}