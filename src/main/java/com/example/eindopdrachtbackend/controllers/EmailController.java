package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam String to) {
        emailService.sendEmail(to, "Hello from IndieVerse", "This is a test email.");
        return ResponseEntity.ok("Email sent successfully");
    }
}
