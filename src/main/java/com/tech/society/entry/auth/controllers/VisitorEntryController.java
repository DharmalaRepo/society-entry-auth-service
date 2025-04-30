// ✅ Updated VisitorEntryController.java for all MVPs
package com.tech.society.entry.auth.controllers;

import com.tech.society.entry.auth.models.VisitorEntry;
import com.tech.society.entry.auth.dto.*;
import com.tech.society.entry.auth.services.VisitorEntryService;
import com.tech.society.entry.auth.utils.ApplicationUtils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visitor-entries")
public class VisitorEntryController {

    @Autowired
    private VisitorEntryService visitorEntryService;

    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(@RequestBody VisitorEntryRequestDTO dto, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.generateOtpEntry(dto, ctx));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOtp(@RequestBody OtpVerificationRequestDTO dto, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.verifyOtp(dto, ctx));
    }

    @GetMapping("/today")
    public ResponseEntity<List<VisitorEntry>> getTodayEntries(HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.getTodayEntries(ctx));
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<List<VisitorEntry>> getPendingRequests(HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.getPendingRequests(ctx));
    }

    @PostMapping("/request-approval")
    public ResponseEntity<Boolean> requestApproval(@RequestBody VisitorEntryRequestDTO dto, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.requestApprovalBySecurity(dto, ctx));
    }

    @PostMapping("/respond-approval")
    public ResponseEntity<Void> respondToApproval(@RequestBody VisitorApprovalResponseDTO dto, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        visitorEntryService.respondToApproval(dto, ctx);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/expire-otps")
    public ResponseEntity<Void> expireOtps(HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        visitorEntryService.expireStaleOtps(ctx);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/exit")
    public ResponseEntity<Void> markExit(@RequestParam String entryId, @RequestParam String time, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        visitorEntryService.markVisitorExit(entryId, LocalDateTime.parse(time), ctx);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Boolean> resendOtp(@RequestParam String entryId, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.resendOtp(entryId, ctx));
    }

    @PostMapping("/cancel-otp")
    public ResponseEntity<Boolean> cancelOtp(@RequestParam String entryId, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.cancelOtp(entryId, ctx));
    }

    @GetMapping("/search")
    public ResponseEntity<List<VisitorEntry>> searchVisitors(@RequestParam String input, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.searchVisitors(input, ctx));
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<Void> uploadPhoto(@RequestParam String entryId, @RequestParam String photoUrl, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        visitorEntryService.uploadVisitorPhoto(entryId, photoUrl, ctx);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/frequent-visitors")
    public ResponseEntity<List<String>> frequentVisitors(@RequestParam String flatId, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.getFrequentVisitorNames(flatId, ctx));
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics(HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.getVisitorAnalytics(ctx));
    }

    @PostMapping("/blacklist")
    public ResponseEntity<Void> blacklist(@RequestParam String mobileOrName, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        visitorEntryService.blacklistVisitor(mobileOrName, ctx);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/is-blacklisted")
    public ResponseEntity<Boolean> isBlacklisted(@RequestParam String mobileOrName, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.isVisitorBlacklisted(mobileOrName, ctx));
    }

    @PostMapping("/suspicious")
    public ResponseEntity<Void> markSuspicious(@RequestParam String entryId, @RequestParam String note, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        visitorEntryService.markSuspiciousEntry(entryId, note, ctx);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/video-call")
    public ResponseEntity<Void> videoCall(@RequestParam String flatId, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        visitorEntryService.initiateVideoCall(flatId, ctx);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/generate-otp-qr")
    public ResponseEntity<String> generateOtpQr(@RequestParam String entryId, HttpServletRequest request) {
        RequestContext ctx = ApplicationUtils.getRequestContext(request);
        return ResponseEntity.ok(visitorEntryService.generateOtpQr(entryId, ctx));
    }
}
