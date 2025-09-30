package com.assessment.login_processing_service.adapter.in;

import com.assessment.login_processing_service.port.in.CustomerLoginAdapterPort;
import com.assessment.login_processing_service.port.in.CustomerLoginPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerLoginAdapter implements CustomerLoginAdapterPort {

    private final CustomerLoginPort customerLoginService;

    @Override
    public void sendLoginTrackingRequest(CostumerLoginAdapterPortModel costumerLogin) {
        customerLoginService.login(mapAdapterPortModelToServicePortModel(costumerLogin));
    }

    private CustomerLoginPort.CostumerLoginPortModel mapAdapterPortModelToServicePortModel(CostumerLoginAdapterPortModel costumerLogin) {
        return new CustomerLoginPort.CostumerLoginPortModel(costumerLogin.customerId(), costumerLogin.username(),
                costumerLogin.client(), costumerLogin.timestamp(), costumerLogin.messageId(), costumerLogin.customerIp());
    }
}