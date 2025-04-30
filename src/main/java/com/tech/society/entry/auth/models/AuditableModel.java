package com.tech.society.entry.auth.models;

import com.tech.society.entry.auth.dto.RequestContext;

import java.time.LocalDateTime;

public abstract class AuditableModel {

    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public void auditCreate(RequestContext ctx) {
        this.createdBy = ctx.getUserName();
        this.createdDate = LocalDateTime.now();
    }

    public void auditModify(RequestContext ctx) {
        this.modifiedBy = ctx.getUserName();
        this.modifiedDate = LocalDateTime.now();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}