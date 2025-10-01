package com.assessment.login_processing_service.port.in;

import com.assessment.login_processing_service.common.model.ClientType;

import java.sql.Timestamp;
import java.util.UUID;

public interface CustomerLoginPort {

    void login(CustomerLoginPortModel customerLogin);

    record CustomerLoginPortModel(UUID customerId, String username, ClientType clientType, Timestamp timestamp,
                                  UUID messageId, String customerIp) {
    }
}
