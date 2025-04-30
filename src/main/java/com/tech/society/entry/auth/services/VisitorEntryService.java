package com.tech.society.entry.auth.services;

import com.tech.society.entry.auth.models.VisitorEntry;
import com.tech.society.entry.auth.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface VisitorEntryService {

    // Phase 1 – MVP
    String generateOtpEntry(VisitorEntryRequestDTO request, RequestContext context);
    boolean verifyOtp(OtpVerificationRequestDTO request, RequestContext context);
    List<VisitorEntry> getTodayEntries(RequestContext context);

    List<VisitorEntry> getPendingRequests(RequestContext context);

    boolean requestApprovalBySecurity(VisitorEntryRequestDTO request, RequestContext context);
    void respondToApproval(VisitorApprovalResponseDTO responseDTO, RequestContext context);
    void expireStaleOtps(RequestContext context);

    // Phase 2
    void markVisitorExit(String entryId, LocalDateTime time, RequestContext context);
    boolean resendOtp(String entryId, RequestContext context);
    boolean cancelOtp(String entryId, RequestContext context);
    List<VisitorEntry> searchVisitors(String mobileOrName, RequestContext context);
    void uploadVisitorPhoto(String entryId, String photoUrl, RequestContext context);
    List<String> getFrequentVisitorNames(String flatId, RequestContext context);

    // Phase 3
    String generateOtpQr(String entryId, RequestContext context);
    Map<String, Object> getVisitorAnalytics(RequestContext context);
    void blacklistVisitor(String mobileOrName, RequestContext context);
    boolean isVisitorBlacklisted(String mobileOrName, RequestContext context);
    String createRecurringPass(Object request, RequestContext context); // stub
    boolean validateRecurringPass(String staffMobile, String gate, LocalDateTime time, RequestContext context);
    void markSuspiciousEntry(String entryId, String note, RequestContext context);
    void initiateVideoCall(String flatId, RequestContext context);
}