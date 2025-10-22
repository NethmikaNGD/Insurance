package com.pg91.insurance.config;

import com.pg91.insurance.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    // Constructor injection for cleaner dependency management
    public SecurityConfig(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // No password encoding - plain text
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // REMOVED: The userDetailsService @Bean – not needed since CustomUserDetailsService is a @Component.
    // Spring auto-detects and qualifies it correctly.

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Permit all static resources
                        .requestMatchers("/css/**", "/image/**", "/favicon.ico", "/js/**", "/assets/**").permitAll()
                        // Permit public pages
                        .requestMatchers("/", "/home", "/plans", "/login", "/error", "/user/register").permitAll()
                        // Permit plan profile for customers (public viewing)
                        .requestMatchers("/planProfile").permitAll()
                        // Admin-only pages
                        .requestMatchers("/dashboard", "/viewPlans", "/newPlan", "/editPlan", "/deletePlan",
                                "/confirmDelete", "/createPlan", "/updatePlan/**", "/auditLogs",
                                "/auditLogsByAction", "/myAuditLogs").hasRole("ADMIN")
                        // Authenticated user pages
                        .requestMatchers("/profile").authenticated()
                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            // Redirect based on user role
                            String role = authentication.getAuthorities().iterator().next().getAuthority();
                            if (role.equals("ROLE_ADMIN")) {
                                response.sendRedirect("/dashboard");
                            } else {
                                response.sendRedirect("/profile");
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .csrf(AbstractHttpConfigurer::disable); // Consider enabling in production
        return http.build();
    }
}