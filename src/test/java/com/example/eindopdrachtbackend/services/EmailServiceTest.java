package com.example.eindopdrachtbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        reset(mailSender);
    }

    @Test
    void sendEmail_WithValidParameters_ShouldCallMailSender() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_WithNullTo_ShouldStillCallMailSender() {
        // Arrange
        String to = null;
        String subject = "Test Subject";
        String body = "Test Body";

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_WithEmptySubject_ShouldCallMailSender() {
        // Arrange
        String to = "test@example.com";
        String subject = "";
        String body = "Test Body";

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_WithNullSubject_ShouldCallMailSender() {
        // Arrange
        String to = "test@example.com";
        String subject = null;
        String body = "Test Body";

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_WithEmptyBody_ShouldCallMailSender() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "";

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_WithNullBody_ShouldCallMailSender() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = null;

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_WithLongEmailAddress_ShouldCallMailSender() {
        // Arrange
        String to = "very.long.email.address.for.testing@example-domain.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
