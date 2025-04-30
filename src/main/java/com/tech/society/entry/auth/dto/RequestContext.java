package com.tech.society.entry.auth.dto;

import lombok.Data;

@Data
public class RequestContext {
    private String societyIdentifier;
    private String ipAddress;
    private String requestTime;

    private String userName;

    public RequestContext() {
    }

    public RequestContext(String societyIdentifier, String ipAddress, String requestTime, String userName) {
        this.societyIdentifier = societyIdentifier;
        this.ipAddress = ipAddress;
        this.requestTime = requestTime;
        this.userName = userName;
    }


    public String getSocietyIdentifier() {
        return societyIdentifier;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public String getUserName() {
        return userName;
    }
}