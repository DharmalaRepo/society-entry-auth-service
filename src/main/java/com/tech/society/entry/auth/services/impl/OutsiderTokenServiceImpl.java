package com.tech.society.entry.auth.services.impl;

import com.tech.society.entry.auth.dto.OutsiderTokenRequest;
import com.tech.society.entry.auth.dto.TokenVerificationResponse;
import com.tech.society.entry.auth.models.OutsiderToken;
import com.tech.society.entry.auth.repositories.OutsiderTokenRepository;
import com.tech.society.entry.auth.services.MailService;
import com.tech.society.entry.auth.services.OutsiderTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OutsiderTokenServiceImpl implements OutsiderTokenService {

    @Autowired
    private OutsiderTokenRepository tokenRepo;

    @Autowired
    private MailService mailService;

    private String visitorEmail = "shivaprasadreddy.dharmala@gmail.com";
    private String residentName = "Shiva Dharmala";

    // Helper method to generate random 4-character alphabetic key
    private String generateTokenKey() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder tokenKey = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            int index = (int) (Math.random() * characters.length());
            tokenKey.append(characters.charAt(index));
        }
        return tokenKey.toString();
    }

    // Helper method to generate random 4-character alphanumeric value
    private String generateTokenValue() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder tokenValue = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            int index = (int) (Math.random() * characters.length());
            tokenValue.append(characters.charAt(index));
        }
        return tokenValue.toString();
    }

    @Override
    public OutsiderToken generateToken(OutsiderTokenRequest request) {
        String tokenKey = generateTokenKey();
        String tokenValue = generateTokenValue();

        OutsiderToken token = new OutsiderToken();
        token.setSocietyId(request.getSocietyId());
        token.setTokenKey(tokenKey);
        token.setTokenValue(tokenValue);
        token.setResidentId(request.getResidentId());
        token.setResidentName(request.getResidentName());
        token.setFlatNumber(request.getFlatNumber());
        token.setMobileNumber(request.getMobileNumber());
        token.setVisitorType(request.getVisitorType());
        token.setPurpose(request.getPurpose());
        token.setValidUntil(LocalDateTime.now().plusHours(2)); // 2 hours validity
        token.setStatus("ACTIVE");
        token.setCreatedDate(LocalDateTime.now());
        token.setCreatedBy(request.getResidentName());
        token.setIsActive(1);

        // Send SMS or notification logic can go here
        // Send email
        mailService.sendTokenHtmlMail(visitorEmail, tokenKey, tokenValue, residentName);
        return tokenRepo.save(token);
    }

    @Override
    public TokenVerificationResponse verifyToken(String tokenKey, String tokenValue) {
        Optional<OutsiderToken> tokenOpt = tokenRepo.findByTokenKeyAndTokenValueAndStatus(tokenKey, tokenValue, "ACTIVE");

        if (tokenOpt.isPresent() && tokenOpt.get().getValidUntil().isAfter(LocalDateTime.now())) {
            OutsiderToken token = tokenOpt.get();
            token.setStatus("USED");
            tokenRepo.save(token);

            return new TokenVerificationResponse("GO", token.getResidentName(), token.getFlatNumber());
        } else {
            return new TokenVerificationResponse("NO-GO", null, null);
        }
    }

    @Override
    @Scheduled(fixedRate = 60000) // every minute, you can adjust this as needed
    public void expireOldTokens() {
        List<OutsiderToken> tokens = tokenRepo.findAll();
        tokens.stream()
                .filter(t -> t.getValidUntil().isBefore(LocalDateTime.now()) && "ACTIVE".equals(t.getStatus()))
                .forEach(t -> {
                    t.setStatus("EXPIRED");
                    tokenRepo.save(t);
                });
    }



}