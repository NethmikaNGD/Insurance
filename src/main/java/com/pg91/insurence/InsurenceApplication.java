package com.pg91.insurence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.pg91")
@EnableJpaRepositories(basePackages = "com.pg91.repo")
@EntityScan(basePackages = "com.pg91.entity")
@EnableJpaAuditing

public class InsurenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InsurenceApplication.class, args);
    }

}