package com.assessment.login_processing_service.core;

import com.assessment.login_processing_service.port.in.CustomerLoginPort;
import com.assessment.login_processing_service.port.out.CustomerTrackingAdapterPort;
import com.assessment.login_processing_service.port.out.CustomerLoginResultDBPort;
import com.assessment.login_processing_service.port.out.PublishLoginEventAdapterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerLoginService implements CustomerLoginPort {

    private final CustomerTrackingAdapterPort customerTrackingAdapterPort;
    private final CustomerLoginResultDBPort customerLoginResultDBPort;
    private final PublishLoginEventAdapterPort publishLoginEventAdapterPort;
    private static final int MAX_NUMBER_OF_REQUEST_TRIES = 3;
    private static final String PUBLISH_TOPIC = "login-tracking-result";

    @Override
    public void login(CustomerLoginPortModel customerLogin) {
        boolean loginSuccessful = false;
        for (int i = 0; i < MAX_NUMBER_OF_REQUEST_TRIES; i++) {
            HttpStatus httpStatus = customerTrackingAdapterPort.sendLoginTrackingRequest(customerLogin.customerId());
            if (httpStatus == HttpStatus.OK) {
                loginSuccessful = true;
                break;
            }
        }
        publishLoginEventAdapterPort.publish(PUBLISH_TOPIC, createAdapterPortModel(customerLogin, loginSuccessful));
        customerLoginResultDBPort.save(createDBPortModel(customerLogin, loginSuccessful));
    }

    private CustomerLoginResultDBPort.CustomerLoginResultPortModel createDBPortModel(CustomerLoginPortModel customerLogin, boolean loginSuccessful) {
        return new CustomerLoginResultDBPort.CustomerLoginResultPortModel(customerLogin.customerId(), customerLogin.username(),
                customerLogin.clientType(), customerLogin.timestamp(), customerLogin.messageId(), customerLogin.customerIp(), loginSuccessful);
    }

    private PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel createAdapterPortModel(CustomerLoginPortModel portModel, boolean loginSuccessful) {
        return new PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel(portModel.customerId(), portModel.username(),
                portModel.clientType(), portModel.timestamp(), portModel.messageId(), portModel.customerIp(), loginSuccessful);
    }
}
