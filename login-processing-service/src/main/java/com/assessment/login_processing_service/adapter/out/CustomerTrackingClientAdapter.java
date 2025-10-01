package com.assessment.login_processing_service.adapter.out;

import com.assessment.login_processing_service.port.out.CustomerTrackingAdapterPort;
import com.assessment.login_processing_service.port.out.CustomerTrackingClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerTrackingClientAdapter implements CustomerTrackingAdapterPort {

    private final CustomerTrackingClientPort customerTrackingClientPort;

    @Override
    public HttpStatus sendLoginTrackingRequest(UUID customerId) {
        return (HttpStatus) customerTrackingClientPort.sendLoginTrackingRequest(customerId).getStatusCode();
    }
}
