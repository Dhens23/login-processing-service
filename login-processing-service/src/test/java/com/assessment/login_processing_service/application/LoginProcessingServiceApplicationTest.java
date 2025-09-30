package com.assessment.login_processing_service.application;

import com.assessment.login_processing_service.config.KafkaTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureTestDatabase
@Import(KafkaTestConfig.class)
class LoginProcessingServiceApplicationTest {

    @Test
    void contextLoads() {

    }
}