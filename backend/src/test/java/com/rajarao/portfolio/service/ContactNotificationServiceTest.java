package com.rajarao.portfolio.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajarao.portfolio.config.ProfileProperties;
import com.rajarao.portfolio.dto.ContactRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ContactNotificationServiceTest {

    @Test
    void sendsContactMessageThroughGmailApiToConfiguredProfileEmail() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        ProfileProperties profile = profile();
        ContactNotificationService service = new ContactNotificationService(
                restClientBuilder, profile, "client-id", "client-secret", "refresh-token");
        ContactRequest request = new ContactRequest(
                "Sender", "sender@example.com", "A request from the portfolio contact form");

        server.expect(requestTo("https://oauth2.googleapis.com/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(content().string(allOf(
                        containsString("client_id=client-id"),
                        containsString("client_secret=client-secret"),
                        containsString("refresh_token=refresh-token"),
                        containsString("grant_type=refresh_token"))))
                .andRespond(withSuccess("{\"access_token\":\"access-token\"}", MediaType.APPLICATION_JSON));
        server.expect(requestTo("https://gmail.googleapis.com/gmail/v1/users/me/messages/send"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(clientRequest -> assertGmailMessage(clientRequest, profile, request))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        service.send(request);

        server.verify();
    }

    @Test
    void failsClearlyWhenGmailOAuthCredentialsAreMissing() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        ContactNotificationService service = new ContactNotificationService(
                restClientBuilder, profile(), "", "", "");

        assertThrows(IllegalStateException.class, () -> service.send(
                new ContactRequest("Sender", "sender@example.com", "A request from the portfolio contact form")));
        server.verify();
    }

    @Test
    void propagatesGoogleOAuthFailures() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        ContactNotificationService service = new ContactNotificationService(
                restClientBuilder, profile(), "client-id", "client-secret", "refresh-token");
        server.expect(requestTo("https://oauth2.googleapis.com/token"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        assertThrows(RestClientResponseException.class, () -> service.send(
                new ContactRequest("Sender", "sender@example.com", "A request from the portfolio contact form")));
        server.verify();
    }

    private void assertGmailMessage(
            org.springframework.http.client.ClientHttpRequest clientRequest,
            ProfileProperties profile,
            ContactRequest contactRequest) throws IOException {
        MockHttpOutputMessage outputMessage = (MockHttpOutputMessage) clientRequest;
        JsonNode payload = new ObjectMapper().readTree(outputMessage.getBodyAsBytes());
        String rawMessage = new String(
                Base64.getUrlDecoder().decode(payload.path("raw").asText()), StandardCharsets.UTF_8);
        String encodedBody = rawMessage.substring(rawMessage.indexOf("\r\n\r\n") + 4).trim();
        String body = new String(Base64.getMimeDecoder().decode(encodedBody), StandardCharsets.UTF_8);

        assertTrue(rawMessage.contains("From: <" + profile.email() + ">"));
        assertTrue(rawMessage.contains("To: <" + profile.email() + ">"));
        assertTrue(rawMessage.contains("Reply-To: <" + contactRequest.email() + ">"));
        assertTrue(body.contains(contactRequest.name()));
        assertTrue(body.contains(contactRequest.message()));
    }

    private ProfileProperties profile() {
        return new ProfileProperties(
                "Rajarao Eedi", "Senior Software Developer", "", "HCLTech",
                "Hyderabad, India", "rajaraoeedi20@gmail.com", "+91-9652977919",
                "https://www.linkedin.com/in/eedi-rajarao-56945025a",
                "https://github.com/Rajaeedi20", "");
    }
}
