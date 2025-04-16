package com.tech.society.entry.auth.controllers;


import com.tech.society.entry.auth.dto.OutsiderTokenRequest;
import com.tech.society.entry.auth.dto.TokenVerificationResponse;
import com.tech.society.entry.auth.models.OutsiderToken;
import com.tech.society.entry.auth.services.OutsiderTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/token")
public class OutsiderTokenController {

    @Autowired
    private OutsiderTokenService tokenService;

    // Generate token endpoint
    @PostMapping("/generate")
    public ResponseEntity<OutsiderToken> generate(@RequestBody OutsiderTokenRequest request) {
        OutsiderToken generatedToken = tokenService.generateToken(request);
        return ResponseEntity.ok(generatedToken);
    }

    // Verify token endpoint
    @PostMapping("/verify")
    public ResponseEntity<TokenVerificationResponse> verify(
            @RequestParam String tokenKey,
            @RequestParam String tokenValue) {
        TokenVerificationResponse response = tokenService.verifyToken(tokenKey, tokenValue);
        return ResponseEntity.ok(response);
    }

    /*


    {
  "societyId": "RID303",
  "residentName": "Ravi Kumar",
  "visitorName": "Swiggy Delivery",
  "visitorEmail": "delivery@example.com",
  "visitorMobile": "9876543210"
}
     */
}