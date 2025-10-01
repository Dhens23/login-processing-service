package com.assessment.login_processing_service.common.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CustomerLoginMessageResult {
    private UUID customerId;
    private String username;
    private ClientType clientType;
    private Timestamp timestamp;
    private UUID messageId;
    private String customerIp;
    private boolean requestResult;
}
