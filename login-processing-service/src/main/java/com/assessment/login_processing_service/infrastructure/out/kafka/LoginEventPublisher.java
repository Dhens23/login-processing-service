package com.assessment.login_processing_service.infrastructure.out.kafka;

import com.assessment.login_processing_service.common.model.CostumerLoginMessageResult;
import com.assessment.login_processing_service.port.out.PublishLoginEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginEventPublisher implements PublishLoginEventPort {

    private final KafkaTemplate<String, CostumerLoginMessageResult> kafkaTemplate;

    public void publish(String topic, CostumerLoginMessageResult message) {
        log.info("Publishing login message to topic: {}, payload: {}", topic, message);

        try {
            kafkaTemplate.send(topic, message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish login event", ex);
                        } else {
                            log.info("Login event published successfully: {}", result.getRecordMetadata());
                        }
                    });
        } catch (Exception e) {
            log.error("Exception while publishing login event", e);
        }
    }
}