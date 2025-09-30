package com.assessment.login_processing_service.infrastructure.in.kafka;

import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.common.model.CustomerLoginMessage;
import com.assessment.login_processing_service.port.in.CustomerLoginAdapterPort;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EnableKafka
@Testcontainers
class LoginEventListenerIntegrationTest {

    private static final String TOPIC = "customer-login";

    @Container
    static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.0");

    @DynamicPropertySource
    static void overrideKafkaProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    private final KafkaTemplate<String, CustomerLoginMessage> kafkaTemplate = createTestKafkaTemplate(kafkaContainer.getBootstrapServers());


    @MockitoBean
    private CustomerLoginAdapterPort customerLoginAdapterPortMock;


    @Test
    void shouldConsumeKafkaMessageAndCallAdapterPort() {
        CustomerLoginMessage inputMessage = new CustomerLoginMessage(UUID.randomUUID(), "test_user",
                ClientType.WEB, 20220101, UUID.randomUUID(), "192.168.0.100");

        CustomerLoginAdapterPort.CostumerLoginAdapterPortModel expectedModel = new CustomerLoginAdapterPort.CostumerLoginAdapterPortModel(
                inputMessage.getCustomerId(), inputMessage.getUsername(), inputMessage.getClientType(),
                new Timestamp(inputMessage.getTimestamp()), inputMessage.getMessageId(), inputMessage.getCustomerIp());

        kafkaTemplate.send(TOPIC, inputMessage);

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> verify(customerLoginAdapterPortMock, times(1)).sendLoginTrackingRequest(expectedModel));
    }


    private KafkaTemplate<String, CustomerLoginMessage> createTestKafkaTemplate(String bootstrapServers) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, CustomerLoginMessage> producerFactory = new DefaultKafkaProducerFactory<>(props);

        return new KafkaTemplate<>(producerFactory);
    }
}