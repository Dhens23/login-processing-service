package com.assessment.login_processing_service.port.out;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public interface CostumerTrackingAdapterPort {

    HttpStatus sendLoginTrackingRequest(UUID customerId);
}
