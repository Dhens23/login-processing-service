package com.assessment.login_processing_service.adapter.out;

import com.assessment.login_processing_service.common.model.CostumerLoginMessageResult;
import com.assessment.login_processing_service.port.out.PublishLoginEventAdapterPort;
import com.assessment.login_processing_service.port.out.PublishLoginEventPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PublishLoginEventAdapter implements PublishLoginEventAdapterPort {

    private final PublishLoginEventPort publishLoginEvent;

    @Override
    public void publish(String topic, CustomerLoginMessageResultPortModel message) {
        publishLoginEvent.publish(topic, mapPortModel(message));
    }

    private CostumerLoginMessageResult mapPortModel(CustomerLoginMessageResultPortModel message) {
        return new CostumerLoginMessageResult(message.customerId(), message.username(), message.clientType(),
                message.timestamp(), message.messageId(), message.customerIp(), message.requestResult());
    }
}
