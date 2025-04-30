package com.tech.society.entry.auth.repositories;

import com.tech.society.entry.auth.models.VisitorEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitorEntryRepository extends MongoRepository<VisitorEntry, String> {


    Optional<VisitorEntry> findByIdAndSocietyIdentifier(String id, String societyIdentifier);
    Optional<VisitorEntry> findByOtpAndOtpActiveTrueAndOtpUsedFalseAndSocietyIdentifier(String otp, String societyIdentifier);

    List<VisitorEntry> findBySocietyIdentifierAndOtpGeneratedAtBetween(
            String societyIdentifier, LocalDateTime start, LocalDateTime end);

    List<VisitorEntry> findBySocietyIdentifierAndApprovalRequestedAtBetween(
            String societyIdentifier, LocalDateTime start, LocalDateTime end);

    List<VisitorEntry> findPendingRequestsBySocietyIdentifier(String societyIdentifier, String approvalStatus);

    List<VisitorEntry> findByOtpActiveTrueAndOtpExpiryAtBeforeAndSocietyIdentifier(LocalDateTime time, String societyIdentifier);
    List<VisitorEntry> findBySocietyIdentifierAndVisitorMobileContainingIgnoreCase(String societyIdentifier, String mobile);
    List<VisitorEntry> findBySocietyIdentifierAndVisitorNameContainingIgnoreCase(String societyIdentifier, String name);

    Optional<VisitorEntry> findByIdAndApprovalRequestedTrueAndSocietyIdentifier(String entryId, String societyIdentifier);

    List<VisitorEntry> findTop10ByFlatIdAndSocietyIdentifierOrderByOtpGeneratedAtDesc(String flatId, String societyIdentifier);

    Optional<VisitorEntry> findByIdAndCheckOutTimeIsNullAndSocietyIdentifier(String entryId, String societyIdentifier);

    Optional<VisitorEntry> findByIdAndOtpActiveTrueAndSocietyIdentifier(String entryId, String societyIdentifier);

    List<VisitorEntry> findBySocietyIdentifier(String societyIdentifier);
}