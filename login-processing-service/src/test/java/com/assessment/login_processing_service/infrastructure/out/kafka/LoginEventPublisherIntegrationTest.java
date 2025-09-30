package com.assessment.login_processing_service.infrastructure.out.kafka;

import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.common.model.CostumerLoginMessageResult;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@EnableKafka
@Testcontainers
class LoginEventPublisherIntegrationTest {

    private static final String TOPIC = "test-topic-publisher";

    @Container
    static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.0");

    @DynamicPropertySource
    static void overrideKafkaBootstrap(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Autowired
    private LoginEventPublisher loginEventPublisher;

    @Test
    void shouldPublishLoginEvent() throws Exception {
        createTestTopic();
        Consumer<String, CostumerLoginMessageResult> consumer = createTestConsumer();
        CostumerLoginMessageResult expectedMessage = sampleLoginMessage();

        loginEventPublisher.publish(TOPIC, expectedMessage);

        // Await until message is received
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    ConsumerRecords<String, CostumerLoginMessageResult> records = consumer.poll(Duration.ofMillis(1000));
                    assertThat(records.count()).isGreaterThan(0);
                    var record = records.iterator().next();
                    assertThat(record.value()).isEqualTo(expectedMessage);
                });

        consumer.close();
    }

    private void createTestTopic() throws Exception {
        try (AdminClient admin = AdminClient.create(Map.of("bootstrap.servers", kafkaContainer.getBootstrapServers()))) {
            admin.createTopics(List.of(new NewTopic(TOPIC, 1, (short) 1))).all().get();
        }
    }

    private Consumer<String, CostumerLoginMessageResult> createTestConsumer() {
        JsonDeserializer<CostumerLoginMessageResult> deserializer = new JsonDeserializer<>(CostumerLoginMessageResult.class);
        deserializer.addTrustedPackages("com.assessment.login_processing_service.common.model");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        ConsumerFactory<String, CostumerLoginMessageResult> factory =
                new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);

        Consumer<String, CostumerLoginMessageResult> consumer = factory.createConsumer();
        consumer.subscribe(List.of(TOPIC));
        return consumer;
    }

    private CostumerLoginMessageResult sampleLoginMessage() {
        return new CostumerLoginMessageResult(
                UUID.randomUUID(),
                "john_doe",
                ClientType.WEB,
                new Timestamp(20220101),
                UUID.randomUUID(),
                "127.0.0.1",
                true
        );
    }
}