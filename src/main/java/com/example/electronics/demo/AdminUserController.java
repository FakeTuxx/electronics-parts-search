package com.example.electronics.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AdminUserController {

    private final AppUserRepository appUserRepository;

    public AdminUserController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/admin/users")
    public String adminUsersPage() {
        return "forward:/admin-users.html";
    }

    @GetMapping("/api/admin/users")
    @ResponseBody
    public List<UserResponse> getUsers() {
        return appUserRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/admin/users/{id}/approve")
    @ResponseBody
    public ResponseEntity<?> approveUser(@PathVariable Long id) {
        Optional<AppUser> optionalUser = appUserRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AppUser user = optionalUser.get();
        user.setStatus(UserStatus.APPROVED);
        appUserRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Benutzer wurde freigegeben."
        ));
    }

    @PostMapping("/api/admin/users/{id}/reject")
    @ResponseBody
    public ResponseEntity<?> rejectUser(@PathVariable Long id) {
        Optional<AppUser> optionalUser = appUserRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AppUser user = optionalUser.get();
        user.setStatus(UserStatus.REJECTED);
        appUserRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Benutzer wurde abgelehnt."
        ));
    }

    public static class UserResponse {
        private Long id;
        private String fullName;
        private String username;
        private String email;
        private String role;
        private String status;
        private String createdAt;

        public static UserResponse fromEntity(AppUser user) {
            UserResponse response = new UserResponse();
            response.id = user.getId();
            response.fullName = user.getFullName();
            response.username = user.getUsername();
            response.email = user.getEmail();
            response.role = user.getRole().name();
            response.status = user.getStatus().name();
            response.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toString() : "";
            return response;
        }

        public Long getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public String getStatus() {
            return status;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}