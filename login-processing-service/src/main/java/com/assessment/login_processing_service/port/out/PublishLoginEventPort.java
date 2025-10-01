package com.assessment.login_processing_service.port.out;

import com.assessment.login_processing_service.common.model.CustomerLoginMessageResult;

public interface PublishLoginEventPort {

    void publish(String topic, CustomerLoginMessageResult message);
}
