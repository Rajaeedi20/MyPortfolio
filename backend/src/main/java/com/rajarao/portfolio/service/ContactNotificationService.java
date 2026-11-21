package com.rajarao.portfolio.service;

import com.rajarao.portfolio.config.ProfileProperties;
import com.rajarao.portfolio.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactNotificationService {

    private final JavaMailSender mailSender;
    private final ProfileProperties profile;
    private final String senderAddress;

    public ContactNotificationService(
            JavaMailSender mailSender,
            ProfileProperties profile,
            @Value("${spring.mail.username:}") String senderAddress) {
        this.mailSender = mailSender;
        this.profile = profile;
        this.senderAddress = senderAddress;
    }

    public void send(ContactRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        if (!senderAddress.isBlank()) {
            message.setFrom(senderAddress);
        }
        message.setTo(profile.email());
        message.setReplyTo(request.email());
        message.setSubject("New portfolio contact form message");
        message.setText("""
                Name: %s
                Email: %s

                Message:
                %s
                """.formatted(request.name(), request.email(), request.message()));
        mailSender.send(message);
    }
}
