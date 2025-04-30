package com.tech.society.entry.auth.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blacklisted_visitors")
public class BlacklistedVisitor {
    @Id
    private String id;
    private String visitorName;
    private String visitorMobile;

    private String societyIdentifier;
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorMobile() {
        return visitorMobile;
    }

    public void setVisitorMobile(String visitorMobile) {
        this.visitorMobile = visitorMobile;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSocietyIdentifier() {
        return societyIdentifier;
    }

    public void setSocietyIdentifier(String societyIdentifier) {
        this.societyIdentifier = societyIdentifier;
    }
}