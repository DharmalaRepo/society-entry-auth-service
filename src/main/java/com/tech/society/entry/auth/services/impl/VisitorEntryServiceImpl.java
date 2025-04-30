package com.tech.society.entry.auth.services.impl;

import com.tech.society.entry.auth.dto.*;
import com.tech.society.entry.auth.models.BlacklistedVisitor;
import com.tech.society.entry.auth.models.RecurringPass;
import com.tech.society.entry.auth.models.VisitorEntry;
import com.tech.society.entry.auth.repositories.BlacklistedVisitorRepository;
import com.tech.society.entry.auth.repositories.RecurringPassRepository;
import com.tech.society.entry.auth.repositories.VisitorEntryRepository;
import com.tech.society.entry.auth.services.MailService;
import com.tech.society.entry.auth.services.VisitorEntryService;
import com.tech.society.entry.auth.services.base.AbstractAuditService;
import com.tech.society.entry.auth.services.notifications.SmsService;
import com.tech.society.entry.auth.services.notifications.WhatsAppService;
import com.tech.society.entry.auth.utils.AppLogger;
import com.tech.society.entry.auth.utils.OtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VisitorEntryServiceImpl extends AbstractAuditService implements VisitorEntryService {

    private static final Logger logger = LoggerFactory.getLogger(VisitorEntryServiceImpl.class);

    @Autowired
    private VisitorEntryRepository repository;

    @Autowired
    private MailService emailService;

    @Autowired
    private SmsService smsService;
    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private BlacklistedVisitorRepository blacklistRepo;

    @Autowired
    private RecurringPassRepository recurringPassRepo;


    @Override
    public String generateOtpEntry(VisitorEntryRequestDTO request, RequestContext ctx) {
        VisitorEntry entry = new VisitorEntry();
        entry.setVisitorName(request.getVisitorName());
        entry.setVisitorMobile(request.getVisitorMobile());
        entry.setVisitorEmail(request.getVisitorEmail());
        entry.setVehicleNumber(request.getVehicleNumber());
        entry.setPurpose(request.getPurpose());
        entry.setFlatId(request.getFlatId());
        entry.setFlatNumber(request.getFlatNumber());
        entry.setBlockNumber(request.getBlockNumber());
        entry.setSocietyIdentifier(ctx.getSocietyIdentifier());
        entry.setCreatedByRole(request.getCreatedByRole());

        String otp = OtpUtil.generateOtp();
        entry.setOtp(otp);
        entry.setOtpGeneratedAt(LocalDateTime.now());
        entry.setOtpExpiryAt(LocalDateTime.now().plusMinutes(30));
        entry.setOtpActive(true);
        entry.setOtpUsed(false);

        auditCreate(entry, ctx);
        repository.save(entry);

        if (entry.getVisitorEmail() != null) {
            emailService.sendOtpToVisitor(
                    entry.getVisitorEmail(),
                    entry.getOtp(),
                    entry.getVisitorName(),
                    "Flat " + entry.getFlatNumber() + ", Block " + entry.getBlockNumber()
            );
        }

        if (entry.getVisitorMobile() != null) {
            smsService.sendOtp(entry.getVisitorMobile(), otp, entry.getVisitorName(), "Flat " + entry.getFlatNumber());
            whatsAppService.sendOtp(entry.getVisitorMobile(), otp, entry.getVisitorName(), "Flat " + entry.getFlatNumber());
        }

        AppLogger.log(logger, "generateOtpEntry", ctx.getUserName(), "OTP generated: " + otp);
        return otp;
    }

    @Override
    public boolean verifyOtp(OtpVerificationRequestDTO request, RequestContext ctx) {
        Optional<VisitorEntry> optional = repository.findByOtpAndOtpActiveTrueAndOtpUsedFalseAndSocietyIdentifier(
                request.getOtp(), ctx.getSocietyIdentifier());

        if (optional.isEmpty()) {
            AppLogger.log(logger, "verifyOtp", ctx.getUserName(), "Invalid/expired OTP: " + request.getOtp());
            return false;
        }

        VisitorEntry entry = optional.get();
        entry.setCheckInTime(LocalDateTime.now());
        entry.setEntryGate(request.getEntryGate());
        entry.setOtpUsed(true);
        entry.setOtpActive(false);
        auditModify(entry, ctx);
        repository.save(entry);

        AppLogger.log(logger, "verifyOtp", ctx.getUserName(), "OTP verified for visitor: " + entry.getVisitorName());
        return true;
    }

    @Override
    public List<VisitorEntry> getTodayEntries(RequestContext ctx) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(6);
        LocalDateTime end = now.plusHours(12);
        List<VisitorEntry> otpEntries = repository.findBySocietyIdentifierAndOtpGeneratedAtBetween(
                ctx.getSocietyIdentifier(), start, end);
        List<VisitorEntry> approvalRequestEntries = repository.findBySocietyIdentifierAndApprovalRequestedAtBetween(
                ctx.getSocietyIdentifier(), start, end);
        otpEntries.addAll(approvalRequestEntries);

        AppLogger.log(logger, "getTodayEntries", ctx.getUserName(), "Fetched " + otpEntries.size() + " entries");
        return otpEntries;
    }

    @Override
    public List<VisitorEntry> getPendingRequests(RequestContext ctx) {
        List<VisitorEntry> entries = repository.findPendingRequestsBySocietyIdentifier( ctx.getSocietyIdentifier(), "PENDING");
        AppLogger.log(logger, "getTodayEntries", ctx.getUserName(), "Fetched " + entries.size() + " entries");
        return entries;
    }

    @Override
    public boolean requestApprovalBySecurity(VisitorEntryRequestDTO request, RequestContext ctx) {
        VisitorEntry entry = new VisitorEntry();
        entry.setVisitorName(request.getVisitorName());
        entry.setVisitorMobile(request.getVisitorMobile());
        entry.setVisitorEmail(request.getVisitorEmail());
        entry.setVehicleNumber(request.getVehicleNumber());
        entry.setPurpose(request.getPurpose());
        entry.setFlatId(request.getFlatId());
        entry.setFlatNumber(request.getFlatNumber());
        entry.setBlockNumber(request.getBlockNumber());
        entry.setSocietyIdentifier(ctx.getSocietyIdentifier());
        entry.setCreatedByRole("SECURITY");

        entry.setApprovalRequested(true);
        entry.setApprovalRequestedAt(LocalDateTime.now());
        entry.setApprovalStatus("PENDING");

        auditCreate(entry, ctx);
        repository.save(entry);

        AppLogger.log(logger, "requestApprovalBySecurity", ctx.getUserName(), "Approval requested for: " + entry.getVisitorName());
        return true;
    }

    @Override
    public void respondToApproval(VisitorApprovalResponseDTO responseDTO, RequestContext ctx) {
        Optional<VisitorEntry> optional = repository.findByIdAndApprovalRequestedTrueAndSocietyIdentifier(
                responseDTO.getEntryId(), ctx.getSocietyIdentifier());

        if (optional.isEmpty()) {
            AppLogger.log(logger, "respondToApproval", ctx.getUserName(), "No pending approval for ID: " + responseDTO.getEntryId());
            return;
        }

        VisitorEntry entry = optional.get();
        entry.setApprovedByResident(responseDTO.isApproved());
        entry.setRejectedByResident(!responseDTO.isApproved());
        entry.setApprovalStatus(responseDTO.isApproved() ? "APPROVED" : "REJECTED");
        entry.setApprovalResponseNote(responseDTO.getNote());
        entry.setApprovalRespondedAt(LocalDateTime.now());

        auditModify(entry, ctx);
        repository.save(entry);

        AppLogger.log(logger, "respondToApproval", ctx.getUserName(), "Approval status updated: " + entry.getApprovalStatus());
    }

    // Stub implementations for other features
    @Override
    public void expireStaleOtps(RequestContext ctx) {
        List<VisitorEntry> staleOtps = repository.findByOtpActiveTrueAndOtpExpiryAtBeforeAndSocietyIdentifier(
                LocalDateTime.now(), ctx.getSocietyIdentifier());

        for (VisitorEntry entry : staleOtps) {
            entry.setOtpActive(false);
            auditModify(entry, ctx);
        }

        repository.saveAll(staleOtps);
        AppLogger.log(logger, "expireStaleOtps", ctx.getUserName(), "Expired " + staleOtps.size() + " stale OTPs");
    }

    @Override
    public void markVisitorExit(String entryId, LocalDateTime time, RequestContext ctx) {
        repository.findByIdAndCheckOutTimeIsNullAndSocietyIdentifier(entryId, ctx.getSocietyIdentifier()).ifPresent(entry -> {
            entry.setCheckOutTime(time);
            auditModify(entry, ctx);
            repository.save(entry);
            AppLogger.log(logger, "markVisitorExit", ctx.getUserName(), "Visitor exit recorded for: " + entry.getVisitorName());
        });
    }

    @Override
    public boolean resendOtp(String entryId, RequestContext ctx) {
        return repository.findByIdAndOtpActiveTrueAndSocietyIdentifier(entryId, ctx.getSocietyIdentifier()).map(entry -> {
            entry.setOtpGeneratedAt(LocalDateTime.now());
            entry.setOtpExpiryAt(LocalDateTime.now().plusMinutes(30));
            auditModify(entry, ctx);
            repository.save(entry);
            AppLogger.log(logger, "resendOtp", ctx.getUserName(), "Resent OTP for: " + entry.getVisitorName());
            return true;
        }).orElse(false);
    }

    @Override
    public boolean cancelOtp(String entryId, RequestContext ctx) {
        return repository.findByIdAndOtpActiveTrueAndSocietyIdentifier(entryId, ctx.getSocietyIdentifier()).map(entry -> {
            entry.setOtpActive(false);
            entry.setOtpExpiryAt(LocalDateTime.now());
            auditModify(entry, ctx);
            repository.save(entry);
            AppLogger.log(logger, "cancelOtp", ctx.getUserName(), "Cancelled OTP for: " + entry.getVisitorName());
            return true;
        }).orElse(false);
    }

    @Override
    public List<VisitorEntry> searchVisitors(String mobileOrName, RequestContext ctx) {
        List<VisitorEntry> results = new ArrayList<>();
        results.addAll(repository.findBySocietyIdentifierAndVisitorMobileContainingIgnoreCase(ctx.getSocietyIdentifier(), mobileOrName));
        results.addAll(repository.findBySocietyIdentifierAndVisitorNameContainingIgnoreCase(ctx.getSocietyIdentifier(), mobileOrName));
        AppLogger.log(logger, "searchVisitors", ctx.getUserName(), "Found " + results.size() + " matching visitors");
        return results;
    }

    @Override
    public void uploadVisitorPhoto(String entryId, String photoUrl, RequestContext ctx) {
        repository.findByIdAndSocietyIdentifier(entryId, ctx.getSocietyIdentifier()).ifPresent(entry -> {
            entry.setPhotoUrl(photoUrl);
            auditModify(entry, ctx);
            repository.save(entry);
            AppLogger.log(logger, "uploadVisitorPhoto", ctx.getUserName(), "Photo uploaded for visitor: " + entry.getVisitorName());
        });
    }

    @Override
    public List<String> getFrequentVisitorNames(String flatId, RequestContext ctx) {
        return repository.findTop10ByFlatIdAndSocietyIdentifierOrderByOtpGeneratedAtDesc(flatId, ctx.getSocietyIdentifier())
                .stream()
                .map(VisitorEntry::getVisitorName)
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public String generateOtpQr(String entryId, RequestContext ctx) {
        return repository.findByIdAndSocietyIdentifier(entryId, ctx.getSocietyIdentifier())
                .map(entry -> {
                    String content = "OTP:" + entry.getOtp() + ";Visitor:" + entry.getVisitorName();
                    return Base64.getEncoder().encodeToString(content.getBytes());
                })
                .orElse(null);
    }

    @Override
    public Map<String, Object> getVisitorAnalytics(RequestContext ctx) {
        List<VisitorEntry> entries = repository.findBySocietyIdentifier(ctx.getSocietyIdentifier());
        long total = entries.size();
        long today = entries.stream().filter(e -> e.getOtpGeneratedAt().toLocalDate().isEqual(LocalDateTime.now().toLocalDate())).count();
        long approved = entries.stream().filter(VisitorEntry::isApprovedByResident).count();
        long rejected = entries.stream().filter(VisitorEntry::isRejectedByResident).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVisitors", total);
        stats.put("todayVisitors", today);
        stats.put("approved", approved);
        stats.put("rejected", rejected);
        return stats;
    }

    @Override
    public void blacklistVisitor(String mobileOrName, RequestContext ctx) {
        BlacklistedVisitor entity = new BlacklistedVisitor();

        // Determine if input is a mobile number or name
        if (mobileOrName.matches("\\d{10}")) {
            entity.setVisitorMobile(mobileOrName);
            entity.setVisitorName("Unknown");
        } else {
            entity.setVisitorName(mobileOrName);
            entity.setVisitorMobile(null);
        }

        entity.setSocietyIdentifier(ctx.getSocietyIdentifier());
        entity.setReason("Manual block by admin");

        auditCreate(entity, ctx);
        blacklistRepo.save(entity);

        AppLogger.log(logger, "blacklistVisitor", ctx.getUserName(), "Blacklisted visitor: " + mobileOrName);
    }

    @Override
    public boolean isVisitorBlacklisted(String mobileOrName, RequestContext ctx) {
        boolean isBlacklisted = blacklistRepo.existsByVisitorMobileOrVisitorNameIgnoreCase(
                mobileOrName, mobileOrName
        );

        AppLogger.log(logger, "isVisitorBlacklisted", ctx.getUserName(),
                "Checked blacklist for: " + mobileOrName + " → " + isBlacklisted);

        return isBlacklisted;
    }

    @Override
    public String createRecurringPass(Object request, RequestContext ctx) {
        if (!(request instanceof RecurringPassRequestDTO dto)) {
            AppLogger.log(logger, "createRecurringPass", ctx.getUserName(), "Invalid request object");
            return null;
        }

        RecurringPass pass = new RecurringPass();
        pass.setVisitorName(dto.getVisitorName());
        pass.setVisitorMobile(dto.getVisitorMobile());
        pass.setFlatId(dto.getFlatId());
        pass.setStartTime(LocalTime.parse(dto.getStartTime()));
        pass.setEndTime(LocalTime.parse(dto.getEndTime()));
        pass.setWeekdays(dto.getWeekdays());
        pass.setSocietyIdentifier(ctx.getSocietyIdentifier());

        auditCreate(pass, ctx);
        recurringPassRepo.save(pass);

        AppLogger.log(logger, "createRecurringPass", ctx.getUserName(), "Recurring pass created for " + dto.getVisitorMobile());
        return pass.getId();
    }

    @Override
    public boolean validateRecurringPass(String staffMobile, String gate, LocalDateTime time, RequestContext ctx) {
        String method = "validateRecurringPass";

        List<RecurringPass> passes = recurringPassRepo.findByVisitorMobileAndSocietyIdentifier(
                staffMobile, ctx.getSocietyIdentifier());

        String dayOfWeek = time.getDayOfWeek().name(); // e.g., "MONDAY"
        LocalTime currentTime = time.toLocalTime();

        boolean isValid = passes.stream().anyMatch(pass ->
                pass.getWeekdays().contains(dayOfWeek) &&
                        currentTime.isAfter(pass.getStartTime()) &&
                        currentTime.isBefore(pass.getEndTime())
        );

        AppLogger.log(logger, method, ctx.getUserName(), "Validation for mobile " + staffMobile + ": " + isValid);
        return isValid;
    }

    @Override
    public void markSuspiciousEntry(String entryId, String note, RequestContext ctx) {
        repository.findByIdAndSocietyIdentifier(entryId, ctx.getSocietyIdentifier()).ifPresent(entry -> {
            entry.setApprovalStatus("SUSPICIOUS");
            entry.setApprovalResponseNote(note);
            auditModify(entry, ctx);
            repository.save(entry);
            AppLogger.log(logger, "markSuspiciousEntry", ctx.getUserName(), "Marked suspicious: " + entry.getVisitorName());
        });
    }

    @Override
    public void initiateVideoCall(String flatId, RequestContext ctx) {
        AppLogger.log(logger, "initiateVideoCall", ctx.getUserName(), "Stub: initiated video call to flat: " + flatId);
    }
}