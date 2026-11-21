package com.rajarao.portfolio.service;

import com.rajarao.portfolio.config.ProfileProperties;
import com.rajarao.portfolio.dto.ContactRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

class ContactNotificationServiceTest {

    @Test
    void sendsContactMessageToConfiguredProfileEmail() {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        ProfileProperties profile = new ProfileProperties(
                "Rajarao Eedi", "Senior Software Developer", "", "HCLTech",
                "Hyderabad, India", "rajaraoeedi20@gmail.com", "+91-9652977919",
                "https://www.linkedin.com/in/eedi-rajarao-56945025a",
                "https://github.com/Rajaeedi20", "");
        ContactNotificationService service = new ContactNotificationService(
                mailSender, profile, "portfolio@example.com");
        ContactRequest request = new ContactRequest(
                "Sender", "sender@example.com", "A request from the portfolio contact form");

        service.send(request);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        SimpleMailMessage message = messageCaptor.getValue();

        assertEquals("portfolio@example.com", message.getFrom());
        assertEquals(profile.email(), message.getTo()[0]);
        assertEquals(request.email(), message.getReplyTo());
        assertTrue(message.getText().contains(request.message()));
    }
}
