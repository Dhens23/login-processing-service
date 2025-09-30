package com.assessment.login_processing_service.adapter.out;

import com.assessment.login_processing_service.port.out.CostumerTrackingClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerTrackingClientAdapterTest {

    private CostumerTrackingClientPort trackingClientPort;
    private CustomerTrackingClientAdapter adapter;

    @BeforeEach
    void setUp() {
        trackingClientPort = mock(CostumerTrackingClientPort.class);
        adapter = new CustomerTrackingClientAdapter(trackingClientPort);
    }

    @Test
    void testSendLoginTrackingRequest_ReturnsHttpStatus() {
        UUID customerId = UUID.randomUUID();
        ResponseEntity<String> mockResponse = new ResponseEntity<>("OK", HttpStatus.ACCEPTED);

        when(trackingClientPort.sendLoginTrackingRequest(customerId))
                .thenReturn(mockResponse);

        HttpStatus result = adapter.sendLoginTrackingRequest(customerId);

        assertEquals(HttpStatus.ACCEPTED, result);
        verify(trackingClientPort, times(1)).sendLoginTrackingRequest(customerId);
    }
}