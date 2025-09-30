package com.assessment.login_processing_service.port.out;

import com.assessment.login_processing_service.common.model.CostumerLoginMessageResult;

public interface PublishLoginEventPort {

    void publish(String topic, CostumerLoginMessageResult message);
}
