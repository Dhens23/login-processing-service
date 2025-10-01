package com.assessment.login_processing_service.port.out;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CustomerTrackingClientPort {

    /**
     * @param customerId ID of customer where login will be tracked
     * @return HttpStatusCode
     */
    ResponseEntity<String> sendLoginTrackingRequest(UUID customerId);
}
