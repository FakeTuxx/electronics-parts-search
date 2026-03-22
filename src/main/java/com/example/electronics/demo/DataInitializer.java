package com.example.electronics.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = getEnvOrDefault("APP_ADMIN_USERNAME", "admin");
            String adminPassword = getEnvOrDefault("APP_ADMIN_PASSWORD", "change-me-now");
            String adminEmail = getEnvOrDefault("APP_ADMIN_EMAIL", "admin@rsg-eps.at");
            String adminFullName = getEnvOrDefault("APP_ADMIN_FULLNAME", "RSG Admin");

            if (appUserRepository.findByUsernameIgnoreCase(adminUsername).isEmpty()) {
                AppUser admin = new AppUser(
                        adminFullName,
                        adminUsername,
                        adminEmail,
                        passwordEncoder.encode(adminPassword),
                        UserRole.ADMIN,
                        UserStatus.APPROVED,
                        LocalDateTime.now()
                );
                appUserRepository.save(admin);
            }
        };
    }

    private String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }
}