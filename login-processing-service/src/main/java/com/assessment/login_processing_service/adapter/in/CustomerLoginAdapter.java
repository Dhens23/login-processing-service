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
    public void sendLoginTrackingRequest(CustomerLoginAdapterPortModel customerLogin) {
        customerLoginService.login(mapAdapterPortModelToServicePortModel(customerLogin));
    }

    private CustomerLoginPort.CustomerLoginPortModel mapAdapterPortModelToServicePortModel(CustomerLoginAdapterPortModel customerLogin) {
        return new CustomerLoginPort.CustomerLoginPortModel(customerLogin.customerId(), customerLogin.username(),
                customerLogin.client(), customerLogin.timestamp(), customerLogin.messageId(), customerLogin.customerIp());
    }
}