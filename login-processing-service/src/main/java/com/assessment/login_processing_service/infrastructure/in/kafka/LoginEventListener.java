package com.assessment.login_processing_service.infrastructure.in.kafka;

import com.assessment.login_processing_service.common.model.CustomerLoginMessage;
import com.assessment.login_processing_service.port.in.CustomerLoginAdapterPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginEventListener {

    private final CustomerLoginAdapterPort customerLoginAdapterPort;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "myGroup", containerFactory = "listenerFactory")
    public void listen(CustomerLoginMessage message) {
        try {
            customerLoginAdapterPort.sendLoginTrackingRequest(mapMessageToPortModel(message));
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }

    private CustomerLoginAdapterPort.CostumerLoginAdapterPortModel mapMessageToPortModel(CustomerLoginMessage message) {
        return new CustomerLoginAdapterPort.CostumerLoginAdapterPortModel(message.getCustomerId(), message.getUsername(),
                message.getClientType(), new Timestamp(message.getTimestamp()),
                message.getMessageId(), message.getCustomerIp());
    }
}