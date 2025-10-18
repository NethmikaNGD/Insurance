package com.yourcompany.medicalsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MedicalSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalSystemApplication.class, args);
        System.out.println("Medical System started.");
    }
}
