package com.assessment.login_processing_service.adapter.in;

import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.port.in.CustomerLoginAdapterPort;
import com.assessment.login_processing_service.port.in.CustomerLoginPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.UUID;

import static org.mockito.Mockito.*;

class CustomerLoginAdapterTest {

    private CustomerLoginPort customerLoginPortMock;
    private CustomerLoginAdapter adapter;


    @BeforeEach
    public void setUp() {
        customerLoginPortMock = mock(CustomerLoginPort.class);
        adapter = new CustomerLoginAdapter(customerLoginPortMock);
    }

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