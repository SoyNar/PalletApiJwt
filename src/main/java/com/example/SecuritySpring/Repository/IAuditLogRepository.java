package com.example.SecuritySpring.Repository;

import com.example.SecuritySpring.Application.Model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IAuditLogRepository extends JpaRepository<AuditLog,Long> {
    List<AuditLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> findByEntityType(String entityType);
    List<AuditLog> findByAction(String action);
    List<AuditLog> findByModifiedBy(String modifiedBy);




}
