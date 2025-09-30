package com.assessment.login_processing_service.infrastructure.out.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Base64;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTrackingClientTest {

    static WireMockServer wireMockServer;

    CustomerTrackingClient client;

    @BeforeAll
    static void setupServer() {
        wireMockServer = new WireMockServer(0); // dynamic port
        wireMockServer.start();
    }

    @AfterAll
    static void tearDownServer() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setupClient() {
        String baseUrl = "http://localhost";
        String port = ":" + wireMockServer.port();

        client = new CustomerTrackingClient();
        client.setBaseUrl(baseUrl);
        client.setPort(port);
        client.setUsername("testUser");
        client.setPassword("testPass");

        client.buildFullBaseUrl();
    }

    @Test
    void shouldSendLoginTrackingRequestSuccessfully() {
        UUID customerId = UUID.randomUUID();
        String expectedPath = "/v1/api/trackLoging/" + customerId;

        wireMockServer.stubFor(post(urlEqualTo(expectedPath))
                .withHeader("Authorization", equalTo("Basic " + encodeCredentials("testUser", "testPass")))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Login tracked")));

        ResponseEntity<String> response = client.sendLoginTrackingRequest(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login tracked", response.getBody());

        wireMockServer.verify(postRequestedFor(urlEqualTo(expectedPath)));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCustomerDoesNotExist() {
        UUID customerId = UUID.randomUUID();
        String expectedPath = "/v1/api/trackLoging/" + customerId;

        wireMockServer.stubFor(post(urlEqualTo(expectedPath))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("Customer not found")));

        HttpClientErrorException.NotFound exception = assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> client.sendLoginTrackingRequest(customerId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getResponseBodyAsString().contains("Customer not found"));
    }

    private static String encodeCredentials(String username, String password) {
        String credentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}