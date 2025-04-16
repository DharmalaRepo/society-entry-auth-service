package com.tech.society.entry.auth.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.tech.society.entry.auth.models.OutsiderToken;

import java.util.Optional;


public interface OutsiderTokenRepository extends MongoRepository<OutsiderToken, String> {
    Optional<OutsiderToken> findByTokenKeyAndTokenValueAndStatus(String tokenKey, String tokenValue, String status);
}