package com.tech.society.entry.auth.services.base;

import com.tech.society.entry.auth.dto.RequestContext;

public abstract class AbstractAuditService {

    protected void auditCreate(Object model, RequestContext ctx) {
        if (model instanceof com.tech.society.entry.auth.models.AuditableModel auditable) {
            auditable.auditCreate(ctx);
        }
    }

    protected void auditModify(Object model, RequestContext ctx) {
        if (model instanceof com.tech.society.entry.auth.models.AuditableModel auditable) {
            auditable.auditModify(ctx);
        }
    }
}