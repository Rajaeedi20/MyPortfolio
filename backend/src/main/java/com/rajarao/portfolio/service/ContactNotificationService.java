package com.rajarao.portfolio.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rajarao.portfolio.config.ProfileProperties;
import com.rajarao.portfolio.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class ContactNotificationService {

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GMAIL_SEND_URL = "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

    private final RestClient restClient;
    private final ProfileProperties profile;
    private final String clientId;
    private final String clientSecret;
    private final String refreshToken;

    public ContactNotificationService(
            RestClient.Builder restClientBuilder,
            ProfileProperties profile,
            @Value("${GMAIL_CLIENT_ID:}") String clientId,
            @Value("${GMAIL_CLIENT_SECRET:}") String clientSecret,
            @Value("${GMAIL_REFRESH_TOKEN:}") String refreshToken) {
        this.restClient = restClientBuilder.build();
        this.profile = profile;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.refreshToken = refreshToken;
    }

    public void send(ContactRequest request) {
        if (clientId.isBlank() || clientSecret.isBlank() || refreshToken.isBlank()) {
            throw new IllegalStateException("Gmail API OAuth credentials are not configured");
        }

        String accessToken = getAccessToken();

        restClient.post()
                .uri(GMAIL_SEND_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("raw", encodeMessage(request)))
                .retrieve()
                .toBodilessEntity();
    }

    private String getAccessToken() {
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("client_id", clientId);
        tokenRequest.add("client_secret", clientSecret);
        tokenRequest.add("refresh_token", refreshToken);
        tokenRequest.add("grant_type", "refresh_token");

        JsonNode response = restClient.post()
                .uri(GOOGLE_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(tokenRequest)
                .retrieve()
                .body(JsonNode.class);

        if (response == null || response.path("access_token").asText().isBlank()) {
            throw new IllegalStateException("Google OAuth response did not contain an access token");
        }
        return response.path("access_token").asText();
    }

    private String encodeMessage(ContactRequest request) {
        String text = """
                Name: %s
                Email: %s

                Message:
                %s
                """.formatted(request.name(), request.email(), request.message());
        String encodedText = Base64.getMimeEncoder(76, "\r\n".getBytes(StandardCharsets.US_ASCII))
                .encodeToString(text.getBytes(StandardCharsets.UTF_8));
        String mimeMessage = "From: <" + profile.email() + ">\r\n"
                + "To: <" + profile.email() + ">\r\n"
                + "Reply-To: <" + request.email() + ">\r\n"
                + "Subject: New portfolio contact form message\r\n"
                + "MIME-Version: 1.0\r\n"
                + "Content-Type: text/plain; charset=UTF-8\r\n"
                + "Content-Transfer-Encoding: base64\r\n"
                + "\r\n"
                + encodedText;
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(mimeMessage.getBytes(StandardCharsets.UTF_8));
    }
}
