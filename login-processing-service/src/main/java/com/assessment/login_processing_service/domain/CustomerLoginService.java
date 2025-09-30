package com.assessment.login_processing_service.domain;

import com.assessment.login_processing_service.port.in.CustomerLoginPort;
import com.assessment.login_processing_service.port.out.CostumerTrackingAdapterPort;
import com.assessment.login_processing_service.port.out.CustomerLoginResultDBPort;
import com.assessment.login_processing_service.port.out.PublishLoginEventAdapterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerLoginService implements CustomerLoginPort {

    private final CostumerTrackingAdapterPort costumerTrackingAdapterPort;
    private final CustomerLoginResultDBPort customerLoginResultDBPort;
    private final PublishLoginEventAdapterPort publishLoginEventAdapterPort;
    private static final int MAX_NUMBER_OF_REQUEST_TRIES = 3;
    static final String PUBLISH_TOPIC = "login-tracking-result";

    @Override
    public void login(CostumerLoginPortModel costumerLogin) {
        boolean loginSuccessful = false;
        for (int i = 0; i < MAX_NUMBER_OF_REQUEST_TRIES; i++) {
            HttpStatus httpStatus = costumerTrackingAdapterPort.sendLoginTrackingRequest(costumerLogin.customerId());
            if (httpStatus == HttpStatus.OK) {
                loginSuccessful = true;
                break;
            }
        }
        publishLoginEventAdapterPort.publish(PUBLISH_TOPIC, createAdapterPortModel(costumerLogin, loginSuccessful));
        customerLoginResultDBPort.save(createModel(costumerLogin, loginSuccessful));
    }

    private CustomerLoginResultDBPort.CustomerLoginResultPortModel createModel(CostumerLoginPortModel costumerLogin, boolean loginSuccessful) {
        return new CustomerLoginResultDBPort.CustomerLoginResultPortModel(costumerLogin.customerId(), costumerLogin.username(),
                costumerLogin.clientType(), costumerLogin.timestamp(), costumerLogin.messageId(), costumerLogin.customerIp(), loginSuccessful);
    }

    private PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel createAdapterPortModel(CostumerLoginPortModel portModel, boolean loginSuccessful) {
        return new PublishLoginEventAdapterPort.CustomerLoginMessageResultPortModel(portModel.customerId(), portModel.username(),
                portModel.clientType(), portModel.timestamp(), portModel.messageId(), portModel.customerIp(), loginSuccessful);
    }
}
