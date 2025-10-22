package com.pg91.insurance.config;

import com.pg91.insurance.repo.AppUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private HttpSession httpSession;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String userEmail = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();

        appUserRepository.findByEmail(userEmail).ifPresent(user -> {
            httpSession.setAttribute("CURRENT_USER_ID", user.getAppUserId());
            System.out.println("CURRENT_USER_ID" + user.getAppUserId());
            httpSession.setAttribute("CURRENT_USER_NAME", user.getName());
            System.out.println("CURRENT_USER_NAME" + user.getName());
            httpSession.setAttribute("CURRENT_USER_EMAIL", user.getEmail());
            System.out.println("CURRENT_USER_EMAIL" + user.getEmail());
        });
    }
}