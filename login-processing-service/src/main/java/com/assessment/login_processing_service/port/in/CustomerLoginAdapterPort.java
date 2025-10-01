package com.assessment.login_processing_service.port.in;

import com.assessment.login_processing_service.common.model.ClientType;

import java.sql.Timestamp;
import java.util.UUID;

public interface CustomerLoginAdapterPort {

    void sendLoginTrackingRequest(CustomerLoginAdapterPortModel customerLogin);

    record CustomerLoginAdapterPortModel(UUID customerId, String username, ClientType client, Timestamp timestamp,
                                         UUID messageId, String customerIp) {
    }
}
