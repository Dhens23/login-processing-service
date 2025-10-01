package com.assessment.login_processing_service.adapter.out;

import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.common.model.CustomerLoginMessageResult;
import com.assessment.login_processing_service.port.out.PublishLoginEventAdapterPort;
import com.assessment.login_processing_service.port.out.PublishLoginEventPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublishLoginEventAdapterTest {

    @Mock
    private PublishLoginEventPort publishLoginEventPortMock;

    @InjectMocks
    private PublishLoginEventAdapter publishLoginEventAdapter;

    @Test
    void shouldPublishMappedLoginEvent() {
        UUID customerId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();
        String username = "john_doe";
        ClientType clientType = ClientType.WEB;
        Timestamp timestamp = new Timestamp(20041212);
        String customerIp = "127.0.0.1";
        boolean requestResult = true;
        String topic = "login-topic";

        PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel message = createCustomerLoginMessageResultPortModel(customerId, username, clientType, timestamp, messageId, customerIp, requestResult);
        CustomerLoginMessageResult expectedCustomerLoginMessageResult = createCustomerLoginMessageResult(customerId, username, clientType, timestamp, messageId, customerIp, requestResult);
        doNothing().when(publishLoginEventPortMock).publish(topic, expectedCustomerLoginMessageResult);

        publishLoginEventAdapter.publish(topic, message);

        verify(publishLoginEventPortMock, times(1)).publish(topic, expectedCustomerLoginMessageResult);
    }

    private PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel createCustomerLoginMessageResultPortModel(UUID customerId, String username, ClientType clientType, Timestamp timestamp, UUID messageId, String customerIp, boolean requestResult) {
        return new PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel(
                customerId,
                username,
                clientType,
                timestamp,
                messageId,
                customerIp,
                requestResult);
    }

    private CustomerLoginMessageResult createCustomerLoginMessageResult(UUID customerId, String username, ClientType clientType, Timestamp timestamp, UUID messageId, String customerIp, boolean requestResult) {
        return new CustomerLoginMessageResult(
                customerId,
                username,
                clientType,
                timestamp,
                messageId,
                customerIp,
                requestResult);
    }
}