package com.pg91.service;

import com.pg91.entity.AuditLog;
import com.pg91.repo.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository repository;

    public AuditLogService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void logAction(Integer userId, String action, String details) {
        AuditLog log = new AuditLog();
        log.setAppUserId(userId);
        log.setAction(action);
        log.setDetails(details);
        repository.save(log);
    }

}