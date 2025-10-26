package com.pg91.insurance.util;

import org.springframework.stereotype.Component;

@Component
public class SimplePasswordEncoder {

    public String encode(CharSequence rawPassword) {
        return String.valueOf(rawPassword);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}