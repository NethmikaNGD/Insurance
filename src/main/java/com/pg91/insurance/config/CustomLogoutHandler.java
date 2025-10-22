package com.pg91.insurance.config;

import com.pg91.insurance.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private AuditLogService auditLogService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            Integer userId = (Integer) session.getAttribute("CURRENT_USER_ID");
            String userEmail = (String) session.getAttribute("CURRENT_USER_EMAIL");

            if (userId != null && userEmail != null) {
                auditLogService.logAction(userId, "USER_LOGOUT",
                        "User logged out: " + userEmail + " (ID: " + userId + ")");
            }
        }
    }
}