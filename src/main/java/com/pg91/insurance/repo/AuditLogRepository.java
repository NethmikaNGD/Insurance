package com.pg91.insurance.repo;

import com.pg91.insurance.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    // Find audit logs by user ID, ordered by creation date (newest first)
    List<AuditLog> findByAppUserIdOrderByCreatedAtDesc(Integer appUserId);

    // Find audit logs by action type, ordered by creation date (newest first)
    List<AuditLog> findByActionOrderByCreatedAtDesc(String action);

    // Find all audit logs ordered by creation date (newest first)
    List<AuditLog> findAllByOrderByCreatedAtDesc();

    // Find audit logs by user ID and action type
    List<AuditLog> findByAppUserIdAndActionOrderByCreatedAtDesc(Integer appUserId, String action);

    // Find audit logs within a date range
    @Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    List<AuditLog> findByCreatedAtBetweenOrderByCreatedAtDesc(@Param("startDate") java.time.LocalDateTime startDate,
                                                              @Param("endDate") java.time.LocalDateTime endDate);

    // Count audit logs by user ID
    long countByAppUserId(Integer appUserId);

    // Count audit logs by action type
    long countByAction(String action);
}