package com.tech.society.entry.auth.repositories;

import com.tech.society.entry.auth.models.RecurringPass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurringPassRepository extends MongoRepository<RecurringPass, String> {
    List<RecurringPass> findByVisitorMobileAndSocietyIdentifier(String mobile, String societyIdentifier);
}