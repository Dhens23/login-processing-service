package com.assessment.login_processing_service.config;

import com.assessment.login_processing_service.common.model.CustomerLoginMessage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.TopicPartitionOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestConfiguration
public class KafkaTestConfig {

    @Bean(name = "testConsumerFactory")
    @Primary
    public ConsumerFactory<String, Object> consumerFactory() {
        return mock(ConsumerFactory.class);
    }

    @Bean(name = "testProducerFactory")
    @Primary
    public ProducerFactory<String, Object> producerFactory() {
        return mock(ProducerFactory.class);
    }

    @Bean(name = "testKafkaTemplate")
    @Primary
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean(name = "testKafkaListenerContainerFactory")
    @Primary
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CustomerLoginMessage>> kafkaListenerContainerFactory() {
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CustomerLoginMessage>> factory = mock(KafkaListenerContainerFactory.class);

        ConcurrentMessageListenerContainer dummyContainer = mock(ConcurrentMessageListenerContainer.class);
        when(dummyContainer.getPhase()).thenReturn(0);
        when(dummyContainer.isRunning()).thenReturn(false);
        doNothing().when(dummyContainer).start();

        // Handle both overloads (varargs ambiguity)
        when(factory.createContainer(any(String[].class))).thenReturn(dummyContainer);
        when(factory.createContainer(any(TopicPartitionOffset[].class))).thenReturn(dummyContainer);

        return factory;
    }
}