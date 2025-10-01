package com.assessment.login_processing_service.adapter.in;

import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.port.in.CustomerLoginAdapterPort;
import com.assessment.login_processing_service.port.in.CustomerLoginPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerLoginAdapterTest {

    @Mock
    private CustomerLoginPort customerLoginPortMock;
    @InjectMocks
    private CustomerLoginAdapter adapter;

    @Test
    void login() {
        UUID customerId = UUID.randomUUID();
        String username = "username";
        ClientType clientType = ClientType.IOS;
        Timestamp timestamp = new Timestamp(20250101);
        UUID messageId = UUID.randomUUID();
        String customerIp = "127.0.0.1";

        CustomerLoginPort.CostumerLoginPortModel loginPortModel = new CustomerLoginPort.CostumerLoginPortModel(
                customerId, username, clientType, timestamp, messageId, customerIp);

        CustomerLoginAdapterPort.CostumerLoginAdapterPortModel loginAdapterPortModel = new CustomerLoginAdapterPort.CostumerLoginAdapterPortModel(
                customerId, username, clientType, timestamp, messageId, customerIp);

        adapter.sendLoginTrackingRequest(loginAdapterPortModel);
        verify(customerLoginPortMock, times(1)).login(loginPortModel);
    }
}