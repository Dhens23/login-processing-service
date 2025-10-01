package com.assessment.login_processing_service.core;

import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.port.in.CustomerLoginPort;
import com.assessment.login_processing_service.port.out.CustomerTrackingAdapterPort;
import com.assessment.login_processing_service.port.out.CustomerLoginResultDBPort;
import com.assessment.login_processing_service.port.out.PublishLoginEventAdapterPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerLoginServiceTest {

    @Mock
    private CustomerTrackingAdapterPort trackingAdapterPortMock;
    @Mock
    private CustomerLoginResultDBPort resultDBPortMock;
    @Mock
    private PublishLoginEventAdapterPort publishLoginEventAdapterPortMock;
    @InjectMocks
    private CustomerLoginService service;

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final String USERNAME = "john.doe";
    private static final ClientType CLIENT_TYPE = ClientType.ANDROID;
    private static final Timestamp TIMESTAMP = new Timestamp(20240101);
    private static final UUID MESSAGE_ID = UUID.randomUUID();
    private static final String CUSTOMER_IP = "192.168.0.1";

    private static final String PUBLISH_TOPIC_TEST = "login-tracking-result";

    @Test
    void testLogin_SuccessOnFirstTry() {
        testLogin(true, HttpStatus.OK);
    }

    @Test
    void testLogin_FailureAfterAllTries() {
        testLogin(false, HttpStatus.CREATED, HttpStatus.ACCEPTED, HttpStatus.NOT_FOUND);
    }

    @Test
    void testLogin_SuccessOnThirdTry() {
        testLogin(true, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.OK);
    }

    private void testLogin(boolean expectedLoginSuccessful, HttpStatus... httpStatuses) {
        CustomerLoginPort.CustomerLoginPortModel loginModel = createCustomerLoginPortModel(CUSTOMER_ID, USERNAME, CLIENT_TYPE, TIMESTAMP, MESSAGE_ID, CUSTOMER_IP);
        CustomerLoginResultDBPort.CustomerLoginResultPortModel resultPortModel = createCustomerLoginResultPortModel(CUSTOMER_ID, USERNAME, CLIENT_TYPE, TIMESTAMP, MESSAGE_ID, CUSTOMER_IP, expectedLoginSuccessful);
        PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel publishPortModel = createPublishPortModel(CUSTOMER_ID, USERNAME, CLIENT_TYPE, TIMESTAMP, MESSAGE_ID, CUSTOMER_IP, expectedLoginSuccessful);

        doNothing().when(publishLoginEventAdapterPortMock).publish(PUBLISH_TOPIC_TEST, publishPortModel);
        // thenReturn parameters split into multiple variables to make this method reusable
        when(trackingAdapterPortMock.sendLoginTrackingRequest(loginModel.customerId()))
                .thenReturn(httpStatuses[0], Arrays.stream(httpStatuses).skip(1).toArray(HttpStatus[]::new));


        service.login(loginModel);

        verify(trackingAdapterPortMock, times(httpStatuses.length)).sendLoginTrackingRequest(loginModel.customerId());
        verify(publishLoginEventAdapterPortMock, times(1)).publish(PUBLISH_TOPIC_TEST, publishPortModel);
        verify(resultDBPortMock, times(1)).save(resultPortModel);
    }

    private CustomerLoginPort.CustomerLoginPortModel createCustomerLoginPortModel(UUID customerId, String username, ClientType clientType,
                                                                                  Timestamp timestamp, UUID messageId, String customerIp) {
        return new CustomerLoginPort.CustomerLoginPortModel(
                customerId,
                username,
                clientType,
                timestamp,
                messageId,
                customerIp
        );
    }

    private CustomerLoginResultDBPort.CustomerLoginResultPortModel createCustomerLoginResultPortModel(UUID customerId, String username, ClientType clientType,
                                                                                                      Timestamp timestamp, UUID messageId, String customerIp,
                                                                                                      boolean loginSuccessful) {
        return new CustomerLoginResultDBPort.CustomerLoginResultPortModel(
                customerId,
                username,
                clientType,
                timestamp,
                messageId,
                customerIp,
                loginSuccessful
        );
    }

    private PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel createPublishPortModel(UUID customerId, String username, ClientType clientType,
                                                                                                    Timestamp timestamp, UUID messageId, String customerIp,
                                                                                                    boolean loginSuccessful) {
        return new PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel(
                customerId,
                username,
                clientType,
                timestamp,
                messageId,
                customerIp,
                loginSuccessful
        );
    }
}