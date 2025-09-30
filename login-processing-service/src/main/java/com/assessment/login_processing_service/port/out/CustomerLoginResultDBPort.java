package com.assessment.login_processing_service.port.out;

import com.assessment.login_processing_service.common.model.ClientType;

import java.sql.Timestamp;
import java.util.UUID;

public interface CustomerLoginResultDBPort {
    void save(CustomerLoginResultPortModel portModel);

    record CustomerLoginResultPortModel(UUID customerId, String username,
                                        ClientType clientType,
                                        Timestamp timestamp, UUID messageId, String customerIp,
                                        boolean loginSuccessful) {
    }
}
