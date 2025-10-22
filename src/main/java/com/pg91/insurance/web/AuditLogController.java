package com.pg91.insurance.web;

import com.pg91.insurance.entity.AuditLog;
import com.pg91.insurance.service.AuditLogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/auditLogs")
    public String viewAuditLogs(Model model, HttpSession session) {
        // Log admin accessing audit logs
        Integer userId = (Integer) session.getAttribute("CURRENT_USER_ID");
        if (userId != null) {
            auditLogService.logAction(userId, "ACCESS_AUDIT_LOGS", "Admin accessed the audit logs page");
        }

        List<AuditLog> auditLogs = auditLogService.getAllAuditLogs();
        model.addAttribute("auditLogs", auditLogs);
        model.addAttribute("totalLogs", auditLogs.size());

        return "adminDashboard/auditLogs";
    }

    @GetMapping("/myAuditLogs")
    public String viewMyAuditLogs(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("CURRENT_USER_ID");
        if (userId != null) {
            // Log admin accessing their own audit logs
            auditLogService.logAction(userId, "ACCESS_MY_AUDIT_LOGS", "Admin accessed their own audit logs");

            List<AuditLog> myAuditLogs = auditLogService.getAuditLogsByUser(userId);
            model.addAttribute("auditLogs", myAuditLogs);
            model.addAttribute("totalLogs", myAuditLogs.size());
            model.addAttribute("isMyLogs", true);
        }

        return "adminDashboard/auditLogs";
    }

    @GetMapping("/auditLogsByAction")
    public String viewAuditLogsByAction(@RequestParam("action") String action, Model model, HttpSession session) {
        // Log admin filtering audit logs by action
        Integer userId = (Integer) session.getAttribute("CURRENT_USER_ID");
        if (userId != null) {
            auditLogService.logAction(userId, "FILTER_AUDIT_LOGS", "Admin filtered audit logs by action: " + action);
        }

        List<AuditLog> auditLogs = auditLogService.getAuditLogsByAction(action);
        model.addAttribute("auditLogs", auditLogs);
        model.addAttribute("totalLogs", auditLogs.size());
        model.addAttribute("filteredAction", action);

        return "adminDashboard/auditLogs";
    }
}
