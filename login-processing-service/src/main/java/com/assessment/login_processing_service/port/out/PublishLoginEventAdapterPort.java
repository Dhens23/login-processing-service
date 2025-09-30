package com.assessment.login_processing_service.port.out;

import com.assessment.login_processing_service.common.model.ClientType;

import java.sql.Timestamp;
import java.util.UUID;

public interface PublishLoginEventAdapterPort {

    void publish(String topic, CustomerLoginMessageResultPortModel message);

    record CustomerLoginMessageResultPortModel(UUID customerId,
                                               String username,
                                               ClientType clientType,
                                               Timestamp timestamp,
                                               UUID messageId,
                                               String customerIp,
                                               boolean requestResult) {
    }
}
