package com.assessment.login_processing_service.adapter.out;

import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.infrastructure.out.db.model.CustomerLoginResult;
import com.assessment.login_processing_service.infrastructure.out.db.repository.CustomerLoginResultRepository;
import com.assessment.login_processing_service.port.out.CustomerLoginResultDBPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerLoginResultDBAdapterTest {

    @Mock
    private CustomerLoginResultRepository repositoryMock;
    @InjectMocks
    private CustomerLoginResultDBAdapter adapter;

    @Test
    void save_shouldMapPortModelAndCallRepositorySave() {
        UUID customerId = UUID.randomUUID();
        String username = "john.doe";
        ClientType clientType = ClientType.IOS;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        UUID messageId = UUID.randomUUID();
        String customerIp = "10.0.0.5";
        boolean loginSuccessful = true;

        CustomerLoginResultDBPort.CustomerLoginResultPortModel portModel = new CustomerLoginResultDBPort.CustomerLoginResultPortModel(
                customerId, username, clientType, timestamp, messageId, customerIp, loginSuccessful);

        CustomerLoginResult expectedLoginResult = new CustomerLoginResult(customerId, username, clientType,
                timestamp, messageId, customerIp, loginSuccessful);

        adapter.save(portModel);
        verify(repositoryMock).save(expectedLoginResult);
    }
}