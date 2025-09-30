package com.assessment.login_processing_service.adapter.out;

import com.assessment.login_processing_service.infrastructure.out.db.model.CustomerLoginResult;
import com.assessment.login_processing_service.infrastructure.out.db.repository.CustomerLoginResultRepository;
import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.port.out.CustomerLoginResultDBPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CustomerLoginResultDBAdapterTest {

    private CustomerLoginResultRepository repositoryMock;
    private CustomerLoginResultDBAdapter adapter;

    @BeforeEach
    void setUp() {
        repositoryMock = mock(CustomerLoginResultRepository.class);
        adapter = new CustomerLoginResultDBAdapter(repositoryMock);
    }

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