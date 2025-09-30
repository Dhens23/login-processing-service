package com.assessment.login_processing_service.infrastructure.in.kafka;


import com.assessment.login_processing_service.adapter.in.CustomerLoginAdapter;
import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.common.model.CustomerLoginMessage;
import com.assessment.login_processing_service.port.in.CustomerLoginAdapterPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class LoginEventListenerTest {

    private LoginEventListener loginEventListener;
    private CustomerLoginAdapter loginAdapterMock;

    @BeforeEach
    public void setUp() {
        loginAdapterMock = mock(CustomerLoginAdapter.class);
        loginEventListener = new LoginEventListener(loginAdapterMock);
    }


    @Test
    public void listen() {
        UUID customerId = UUID.randomUUID();
        String username = "username";
        ClientType clientType = ClientType.WEB;
        int timestamp = 20250101;
        UUID messageId = UUID.randomUUID();
        String customerIp = "helloWorld";

        CustomerLoginMessage message = new CustomerLoginMessage(customerId, username, clientType, timestamp, messageId, customerIp);
        CustomerLoginAdapterPort.CostumerLoginAdapterPortModel expectedPortModel = new CustomerLoginAdapterPort.CostumerLoginAdapterPortModel(
                customerId, username, clientType, new Timestamp(timestamp), messageId, customerIp);

        loginEventListener.listen(message);
        verify(loginAdapterMock, times(1)).sendLoginTrackingRequest(expectedPortModel);
    }

    @Test
    public void listen_ExceptionInLogin() {
        CustomerLoginMessage message = new CustomerLoginMessage(UUID.randomUUID(), "username", ClientType.WEB, 20250101, UUID.randomUUID(), "helloWorld");

        doThrow(new RuntimeException("Something went wrong")).when(loginAdapterMock).sendLoginTrackingRequest(any());

        assertDoesNotThrow(() -> loginEventListener.listen(message));
        verify(loginAdapterMock, times(1)).sendLoginTrackingRequest(any());
    }
}