package com.pg91.insurance.service;

import com.pg91.insurance.entity.AuditLog;
import com.pg91.insurance.repo.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    // Enhanced logging methods for different types of actions
    public void logPlanAction(Integer userId, String action, String planTitle, Integer planId, String planCode) {
        String details = String.format("Plan: %s (ID: %d, Code: %s)", planTitle, planId, planCode);
        logAction(userId, action, details);
    }

    public void logDashboardAccess(Integer userId, String section) {
        logAction(userId, "ACCESS_DASHBOARD", "Accessed dashboard section: " + section);
    }

    public void logViewAction(Integer userId, String entityType, String entityName, Integer entityId) {
        String details = String.format("Viewed %s: %s (ID: %d)", entityType, entityName, entityId);
        logAction(userId, "VIEW_" + entityType.toUpperCase(), details);
    }

    public void logCreateAction(Integer userId, String entityType, String entityName, Integer entityId) {
        String details = String.format("Created %s: %s (ID: %d)", entityType, entityName, entityId);
        logAction(userId, "CREATE_" + entityType.toUpperCase(), details);
    }

    public void logUpdateAction(Integer userId, String entityType, String entityName, Integer entityId, String changes) {
        String details = String.format("Updated %s: %s (ID: %d) - Changes: %s", entityType, entityName, entityId, changes);
        logAction(userId, "UPDATE_" + entityType.toUpperCase(), details);
    }

    public void logDeleteAction(Integer userId, String entityType, String entityName, Integer entityId) {
        String details = String.format("Deleted %s: %s (ID: %d)", entityType, entityName, entityId);
        logAction(userId, "DELETE_" + entityType.toUpperCase(), details);
    }

    // Get audit logs for a specific user
    public List<AuditLog> getAuditLogsByUser(Integer userId) {
        return repository.findByAppUserIdOrderByCreatedAtDesc(userId);
    }

    // Get audit logs for a specific action
    public List<AuditLog> getAuditLogsByAction(String action) {
        return repository.findByActionOrderByCreatedAtDesc(action);
    }

    // Get all audit logs
    public List<AuditLog> getAllAuditLogs() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
}