package com.assessment.login_processing_service.adapter.out;

import com.assessment.login_processing_service.infrastructure.out.db.model.CustomerLoginResult;
import com.assessment.login_processing_service.infrastructure.out.db.repository.CustomerLoginResultRepository;
import com.assessment.login_processing_service.port.out.CustomerLoginResultDBPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerLoginResultDBAdapter implements CustomerLoginResultDBPort {

    private final CustomerLoginResultRepository customerLoginResultRepository;

    @Override
    public void save(CustomerLoginResultPortModel portModel) {
        customerLoginResultRepository.save(mapToEntity(portModel));
    }

    private CustomerLoginResult mapToEntity(CustomerLoginResultPortModel portModel) {
        return new CustomerLoginResult(portModel.customerId(), portModel.username(), portModel.clientType(),
                portModel.timestamp(), portModel.messageId(), portModel.customerIp(), portModel.loginSuccessful());
    }
}

