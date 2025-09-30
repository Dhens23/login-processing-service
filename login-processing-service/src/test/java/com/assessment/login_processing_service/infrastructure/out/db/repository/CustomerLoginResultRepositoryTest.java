package com.assessment.login_processing_service.infrastructure.out.db.repository;

import com.assessment.login_processing_service.common.model.ClientType;
import com.assessment.login_processing_service.infrastructure.out.db.model.CustomerLoginResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerLoginResultRepositoryTest {

    @Autowired
    private CustomerLoginResultRepository customerLoginResultRepository;

    @Test
    void findById() {
        CustomerLoginResult username = new CustomerLoginResult(UUID.randomUUID(), "username", ClientType.IOS, new Timestamp(20250101),
                UUID.randomUUID(), "127.0.0.1", true);

        customerLoginResultRepository.save(username);

        Optional<CustomerLoginResult> customerLoginResult = customerLoginResultRepository.findById(username.getCustomerLoginResultId());

        assertTrue(customerLoginResult.isPresent());

        assertNotNull(customerLoginResult.get().getCustomerId());
        assertEquals("username", customerLoginResult.get().getUsername());
        assertEquals(ClientType.IOS, customerLoginResult.get().getClientType());
        assertEquals(new Timestamp(20250101), customerLoginResult.get().getTimestamp());
        assertNotNull(customerLoginResult.get().getMessageId());
        assertEquals("127.0.0.1", customerLoginResult.get().getCustomerIp());
        assertTrue(customerLoginResult.get().isLoginSuccessful());
    }
}