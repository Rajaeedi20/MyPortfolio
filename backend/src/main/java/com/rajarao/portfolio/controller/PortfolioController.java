package com.rajarao.portfolio.controller;

import com.rajarao.portfolio.config.ProfileProperties;
import com.rajarao.portfolio.service.ContactNotificationService;
import com.rajarao.portfolio.dto.ContactRequest;
import com.rajarao.portfolio.model.*;
import com.rajarao.portfolio.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PortfolioController {

    private static final Logger log = LoggerFactory.getLogger(PortfolioController.class);

    private final ProfileProperties profile;
    private final ExperienceRepository experiences;
    private final ProjectRepository projects;
    private final SkillGroupRepository skills;
    private final ContactMessageRepository contacts;
    private final ContactNotificationService contactNotifications;

    @GetMapping("/profile")
    public ProfileProperties profile() {
        return profile;
    }

    @GetMapping("/experience")
    public List<Experience> experience() {
        return experiences.findAllByOrderBySortOrderAsc();
    }

    @GetMapping("/projects")
    public List<Project> projects() {
        return projects.findAllByOrderBySortOrderAsc();
    }

    @GetMapping("/skills")
    public List<SkillGroup> skills() {
        return skills.findAllByOrderBySortOrderAsc();
    }

    @PostMapping("/contact")
    public ResponseEntity<Map<String, String>> contact(@Valid @RequestBody ContactRequest req) {
        contacts.save(ContactMessage.builder()
                .name(req.name()).email(req.email()).message(req.message())
                .createdAt(Instant.now()).build());

        try {
            contactNotifications.send(req);
        } catch (RestClientException | IllegalStateException ex) {
            log.error("Contact message was saved, but its email notification could not be delivered", ex);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status", "received", "emailSent", "false",
                            "message", "Your message was received, but the email notification could not be sent. Please email me directly."));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("status", "ok", "emailSent", "true",
                        "message", "Thanks! Your message has been sent to Rajarao Eedi."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalid(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("status", "error",
                        "message", "Please check your name, email and message (min 10 characters)."));
    }
}
