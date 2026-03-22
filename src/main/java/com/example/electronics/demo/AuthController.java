package com.example.electronics.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class AuthController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "forward:/login.html";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "forward:/register.html";
    }

    @GetMapping("/api/auth/me")
    @ResponseBody
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "authenticated", false
            ));
        }

        AppUser user = appUserRepository.findByUsernameIgnoreCase(authentication.getName()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "authenticated", false
            ));
        }

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "username", user.getUsername(),
                "fullName", user.getFullName(),
                "role", user.getRole().name(),
                "status", user.getStatus().name()
        ));
    }

    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String fullName = safe(request.getFullName());
        String username = safe(request.getUsername()).toLowerCase();
        String email = safe(request.getEmail()).toLowerCase();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();

        if (fullName.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Bitte alle Felder ausfüllen."
            ));
        }

        if (username.length() < 3) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Der Benutzername muss mindestens 3 Zeichen haben."
            ));
        }

        if (password.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Das Passwort muss mindestens 6 Zeichen haben."
            ));
        }

        if (appUserRepository.findByUsernameIgnoreCase(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "success", false,
                    "message", "Dieser Benutzername existiert bereits."
            ));
        }

        if (appUserRepository.findByEmailIgnoreCase(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "success", false,
                    "message", "Diese E-Mail-Adresse existiert bereits."
            ));
        }

        AppUser newUser = new AppUser(
                fullName,
                username,
                email,
                passwordEncoder.encode(password),
                UserRole.USER,
                UserStatus.PENDING,
                LocalDateTime.now()
        );

        appUserRepository.save(newUser);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Registrierung erfolgreich. Dein Konto wartet jetzt auf Freigabe durch den Administrator."
        ));
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    public static class RegisterRequest {
        private String fullName;
        private String username;
        private String email;
        private String password;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}