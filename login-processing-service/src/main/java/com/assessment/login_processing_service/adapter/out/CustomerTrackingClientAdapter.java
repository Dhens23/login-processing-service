package com.assessment.login_processing_service.adapter.out;

import com.assessment.login_processing_service.port.out.CostumerTrackingAdapterPort;
import com.assessment.login_processing_service.port.out.CostumerTrackingClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerTrackingClientAdapter implements CostumerTrackingAdapterPort {

    private final CostumerTrackingClientPort customerTrackingClientPort;

    @Override
    public HttpStatus sendLoginTrackingRequest(UUID customerId) {
        return (HttpStatus) customerTrackingClientPort.sendLoginTrackingRequest(customerId).getStatusCode();
    }
}
