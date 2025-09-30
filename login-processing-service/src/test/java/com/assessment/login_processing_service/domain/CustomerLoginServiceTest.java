package com.assessment.login_processing_service.domain;

import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.port.in.CustomerLoginPort;
import com.assessment.login_processing_service.port.out.CostumerTrackingAdapterPort;
import com.assessment.login_processing_service.port.out.CustomerLoginResultDBPort;
import com.assessment.login_processing_service.port.out.PublishLoginEventAdapterPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.*;

class CustomerLoginServiceTest {

    private CostumerTrackingAdapterPort trackingAdapterPortMock;
    private CustomerLoginResultDBPort resultDBPortMock;
    private PublishLoginEventAdapterPort publishLoginEventAdapterPortMock;
    private CustomerLoginService service;

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final String USERNAME = "john.doe";
    private static final ClientType CLIENT_TYPE = ClientType.ANDROID;
    private static final Timestamp TIMESTAMP = new Timestamp(20240101);
    private static final UUID MESSAGE_ID = UUID.randomUUID();
    private static final String CUSTOMER_IP = "192.168.0.1";

    @BeforeEach
    void setUp() {
        trackingAdapterPortMock = mock(CostumerTrackingAdapterPort.class);
        resultDBPortMock = mock(CustomerLoginResultDBPort.class);
        publishLoginEventAdapterPortMock = mock(PublishLoginEventAdapterPort.class);
        service = new CustomerLoginService(trackingAdapterPortMock, resultDBPortMock, publishLoginEventAdapterPortMock);
    }

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
        CustomerLoginPort.CostumerLoginPortModel loginModel = createCostumerLoginPortModel(CUSTOMER_ID, USERNAME, CLIENT_TYPE, TIMESTAMP, MESSAGE_ID, CUSTOMER_IP);
        CustomerLoginResultDBPort.CustomerLoginResultPortModel resultPortModel = createCustomerLoginResultPortModel(CUSTOMER_ID, USERNAME, CLIENT_TYPE, TIMESTAMP, MESSAGE_ID, CUSTOMER_IP, expectedLoginSuccessful);
        PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel publishPortModel = createPublishPortModel(CUSTOMER_ID, USERNAME, CLIENT_TYPE, TIMESTAMP, MESSAGE_ID, CUSTOMER_IP, expectedLoginSuccessful);

        doNothing().when(publishLoginEventAdapterPortMock).publish(CustomerLoginService.PUBLISH_TOPIC, publishPortModel);
        // thenReturn parameters split into multiple variables to make this method reusable
        when(trackingAdapterPortMock.sendLoginTrackingRequest(loginModel.customerId()))
                .thenReturn(httpStatuses[0], Arrays.stream(httpStatuses).skip(1).toArray(HttpStatus[]::new));


        service.login(loginModel);

        verify(trackingAdapterPortMock, times(httpStatuses.length)).sendLoginTrackingRequest(loginModel.customerId());
        verify(publishLoginEventAdapterPortMock, times(1)).publish(CustomerLoginService.PUBLISH_TOPIC, publishPortModel);
        verify(resultDBPortMock, times(1)).save(resultPortModel);
    }

    private CustomerLoginPort.CostumerLoginPortModel createCostumerLoginPortModel(UUID customerId, String username, ClientType clientType,
                                                                                  Timestamp timestamp, UUID messageId, String customerIp) {
        return new CustomerLoginPort.CostumerLoginPortModel(
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