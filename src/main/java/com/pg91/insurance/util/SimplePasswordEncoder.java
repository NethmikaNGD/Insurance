package com.pg91.insurance.util;

import org.springframework.stereotype.Component;

@Component
public class SimplePasswordEncoder {

    public String encode(CharSequence rawPassword) {
        // For development purposes only - use proper password encoding in production
        return String.valueOf(rawPassword);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}