package com.example.SecuritySpring.Services;

import com.example.SecuritySpring.Application.Model.AuditLog;

import java.time.LocalDateTime;
import java.util.List;

public interface IAuditLogService {
    void logAction(String action, String entityType, Long entityId, String details);
    List<AuditLog> getAuditLogsByDate(LocalDateTime startDate, LocalDateTime endDate);
    byte[] generateAuditReport(LocalDateTime date);
    List<AuditLog> getAuditLogsByEntity(String entityType, Long entityId);


}
