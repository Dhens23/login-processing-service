package com.assessment.login_processing_service.adapter.out;

import com.assessment.login_processing_service.port.out.CostumerTrackingClientPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerTrackingClientAdapterTest {

    @Mock
    private CostumerTrackingClientPort trackingClientPortMock;
    @InjectMocks
    private CustomerTrackingClientAdapter adapter;

    @Test
    void testSendLoginTrackingRequest_ReturnsHttpStatus() {
        UUID customerId = UUID.randomUUID();
        ResponseEntity<String> mockResponse = new ResponseEntity<>("OK", HttpStatus.ACCEPTED);

        when(trackingClientPortMock.sendLoginTrackingRequest(customerId))
                .thenReturn(mockResponse);

        HttpStatus result = adapter.sendLoginTrackingRequest(customerId);

        assertEquals(HttpStatus.ACCEPTED, result);
        verify(trackingClientPortMock, times(1)).sendLoginTrackingRequest(customerId);
    }
}