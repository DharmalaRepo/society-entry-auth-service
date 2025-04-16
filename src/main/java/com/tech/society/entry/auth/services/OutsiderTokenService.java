package com.tech.society.entry.auth.services;

import com.tech.society.entry.auth.dto.OutsiderTokenRequest;
import com.tech.society.entry.auth.dto.TokenVerificationResponse;
import com.tech.society.entry.auth.models.OutsiderToken;

public interface OutsiderTokenService {
    OutsiderToken generateToken(OutsiderTokenRequest request);
    TokenVerificationResponse verifyToken(String tokenKey, String tokenValue);
    void expireOldTokens(); // optional scheduled task to expire old tokens
}