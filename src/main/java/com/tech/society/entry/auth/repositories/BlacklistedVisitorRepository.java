package com.tech.society.entry.auth.repositories;

import com.tech.society.entry.auth.models.BlacklistedVisitor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistedVisitorRepository extends MongoRepository<BlacklistedVisitor, String> {
    boolean existsByVisitorMobileOrVisitorNameIgnoreCase(String mobile, String name);
}
