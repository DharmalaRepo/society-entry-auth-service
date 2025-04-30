package com.tech.society.entry.auth.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "visitor_entries")
public class VisitorEntry extends AuditableModel {

    @Id
    private String id;

    private String customId;
    private String visitorName;
    private String visitorMobile;
    private String visitorEmail;
    private String vehicleNumber;
    private String purpose;

    private String flatId;
    private String flatNumber;
    private String blockNumber;
    private String flatOwnerName;
    private String societyIdentifier;

    private String otp;
    private LocalDateTime otpGeneratedAt;
    private LocalDateTime otpExpiryAt;
    private boolean otpUsed;
    private boolean otpActive;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String entryGate;
    private String photoUrl;
    private String createdByRole;

    // Approval Flow
    private boolean approvalRequested;
    private boolean approvedByResident;
    private boolean rejectedByResident;
    private LocalDateTime approvalRequestedAt;
    private LocalDateTime approvalRespondedAt;
    private String approvalResponseNote;
    private String approvalStatus; // PENDING, APPROVED, REJECTED



    public VisitorEntry() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
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

    public String getVisitorEmail() {
        return visitorEmail;
    }

    public void setVisitorEmail(String visitorEmail) {
        this.visitorEmail = visitorEmail;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getFlatId() {
        return flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getFlatOwnerName() {
        return flatOwnerName;
    }

    public void setFlatOwnerName(String flatOwnerName) {
        this.flatOwnerName = flatOwnerName;
    }

    public String getSocietyIdentifier() {
        return societyIdentifier;
    }

    public void setSocietyIdentifier(String societyIdentifier) {
        this.societyIdentifier = societyIdentifier;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpGeneratedAt() {
        return otpGeneratedAt;
    }

    public void setOtpGeneratedAt(LocalDateTime otpGeneratedAt) {
        this.otpGeneratedAt = otpGeneratedAt;
    }

    public LocalDateTime getOtpExpiryAt() {
        return otpExpiryAt;
    }

    public void setOtpExpiryAt(LocalDateTime otpExpiryAt) {
        this.otpExpiryAt = otpExpiryAt;
    }

    public boolean isOtpUsed() {
        return otpUsed;
    }

    public void setOtpUsed(boolean otpUsed) {
        this.otpUsed = otpUsed;
    }

    public boolean isOtpActive() {
        return otpActive;
    }

    public void setOtpActive(boolean otpActive) {
        this.otpActive = otpActive;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getEntryGate() {
        return entryGate;
    }

    public void setEntryGate(String entryGate) {
        this.entryGate = entryGate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCreatedByRole() {
        return createdByRole;
    }

    public void setCreatedByRole(String createdByRole) {
        this.createdByRole = createdByRole;
    }

    public boolean isApprovalRequested() {
        return approvalRequested;
    }

    public void setApprovalRequested(boolean approvalRequested) {
        this.approvalRequested = approvalRequested;
    }

    public boolean isApprovedByResident() {
        return approvedByResident;
    }

    public void setApprovedByResident(boolean approvedByResident) {
        this.approvedByResident = approvedByResident;
    }

    public boolean isRejectedByResident() {
        return rejectedByResident;
    }

    public void setRejectedByResident(boolean rejectedByResident) {
        this.rejectedByResident = rejectedByResident;
    }

    public LocalDateTime getApprovalRequestedAt() {
        return approvalRequestedAt;
    }

    public void setApprovalRequestedAt(LocalDateTime approvalRequestedAt) {
        this.approvalRequestedAt = approvalRequestedAt;
    }

    public LocalDateTime getApprovalRespondedAt() {
        return approvalRespondedAt;
    }

    public void setApprovalRespondedAt(LocalDateTime approvalRespondedAt) {
        this.approvalRespondedAt = approvalRespondedAt;
    }

    public String getApprovalResponseNote() {
        return approvalResponseNote;
    }

    public void setApprovalResponseNote(String approvalResponseNote) {
        this.approvalResponseNote = approvalResponseNote;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }


}