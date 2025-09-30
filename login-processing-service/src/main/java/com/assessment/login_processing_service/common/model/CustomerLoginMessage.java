package com.assessment.login_processing_service.common.model;

import com.assessment.login_processing_service.infrastructure.in.kafka.ClientTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoginMessage {
    private UUID customerId;
    private String username;
    @JsonDeserialize(using = ClientTypeDeserializer.class)
    private ClientType clientType;
    private Integer timestamp;
    private UUID messageId;
    private String customerIp;
}