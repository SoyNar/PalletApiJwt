package com.example.SecuritySpring.Services.Impl;

import com.example.SecuritySpring.Application.Model.AuditLog;
import com.example.SecuritySpring.Repository.IAuditLogRepository;
import com.example.SecuritySpring.Services.IAuditLogService;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Document;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AuditLogServiceImpl implements IAuditLogService {


    @Autowired
    private IAuditLogRepository auditLogRepository;

    @Override
    public void logAction(String action, String entityType, Long entityId, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setDetails(details);

        auditLogRepository.save(auditLog);
        log.info("Audit log created: {} - {} - {}", action, entityType, details);
    }

    @Override
    public List<AuditLog> getAuditLogsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate);
    }

    @Override
    public byte[] generateAuditReport(LocalDateTime date) {
        // Establecer el rango de fechas para el día especificado
        LocalDateTime startOfDay = date.with(LocalTime.MIN);
        LocalDateTime endOfDay = date.with(LocalTime.MAX);

        // Obtener los registros de auditoría
        List<AuditLog> auditLogs = getAuditLogsByDate(startOfDay, endOfDay);

        // Generar PDF con los registros
        return generatePdfReport(auditLogs);
    }

    @Override
    public List<AuditLog> getAuditLogsByEntity(String entityType, Long entityId) {
        return List.of();
    }

    private byte[] generatePdfReport(List<AuditLog> auditLogs) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);

            document.open();

            // Agregar título
            Paragraph title = new Paragraph("Audit Log Report");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Crear tabla
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            // Agregar encabezados
            table.addCell("Timestamp");
            table.addCell("Action");
            table.addCell("Entity Type");
            table.addCell("Entity ID");
            table.addCell("Modified By");

            // Agregar datos
            for (AuditLog log : auditLogs) {
                table.addCell(log.getTimestamp().toString());
                table.addCell(log.getAction());
                table.addCell(log.getEntityType());
                table.addCell(log.getEntityId().toString());
                table.addCell(log.getModifiedBy());
            }

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            log.error("Error generating PDF report", e);
            throw new RuntimeException("Error generating PDF report", e);
        }
    }
}
