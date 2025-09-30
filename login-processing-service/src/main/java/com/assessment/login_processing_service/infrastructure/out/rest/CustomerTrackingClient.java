package com.assessment.login_processing_service.infrastructure.out.rest;

import com.assessment.login_processing_service.port.out.CostumerTrackingClientPort;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Setter(AccessLevel.PACKAGE)
public class CustomerTrackingClient implements CostumerTrackingClientPort {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${customer.tracking.base-url}")
    private String baseUrl;

    @Value("${customer.tracking.port}")
    private String port;

    @Value("${customer.tracking.username}")
    private String username;

    @Value("${customer.tracking.password}")
    private String password;

    private String fullBaseUrl;

    private static final String RELATIVE_URL = "/v1/api/trackLoging/";

    @PostConstruct
    void buildFullBaseUrl() {
        if (baseUrl.endsWith("/") && port.startsWith(":")) {
            fullBaseUrl = baseUrl.substring(0, baseUrl.length() - 1) + port;
        } else {
            fullBaseUrl = baseUrl + port;
        }
    }

    public ResponseEntity<String> sendLoginTrackingRequest(UUID customerId) {
        String url = fullBaseUrl + RELATIVE_URL + customerId;

        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedCredentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}