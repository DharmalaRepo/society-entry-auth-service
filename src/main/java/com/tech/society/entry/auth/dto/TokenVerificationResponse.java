package com.tech.society.entry.auth.dto;

public class TokenVerificationResponse {
    private String result; // GO / NO-GO
    private String residentName;
    private String flatNumber;

    // Constructor
    public TokenVerificationResponse(String result, String residentName, String flatNumber) {
        this.result = result;
        this.residentName = residentName;
        this.flatNumber = flatNumber;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    // Getters, Setters, toString
}
