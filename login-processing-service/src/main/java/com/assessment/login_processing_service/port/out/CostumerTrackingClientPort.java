package com.assessment.login_processing_service.port.out;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CostumerTrackingClientPort {

    /**
     * @param customerId ID of costumer where login will be tracked
     * @return HttpStatusCode
     */
    ResponseEntity<String> sendLoginTrackingRequest(UUID customerId);
}
