package com.assessment.login_processing_service.infrastructure.out.db.model;

import com.assessment.login_processing_service.common.model.ClientType;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "customer_login_result")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class CustomerLoginResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_login_result_id", nullable = false, updatable = false)
    private UUID customerLoginResultId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "username", nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false)
    private ClientType clientType;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "message_id", nullable = false)
    private UUID messageId;

    @Column(name = "customer_ip", nullable = false)
    private String customerIp;

    @Column(name = "login_successful", nullable = false)
    private boolean loginSuccessful;

    public CustomerLoginResult(UUID customerId, String username, ClientType clientType, Timestamp timestamp, UUID messageId, String customerIp, boolean loginSuccessful) {
        this.customerId = customerId;
        this.username = username;
        this.clientType = clientType;
        this.timestamp = timestamp;
        this.messageId = messageId;
        this.customerIp = customerIp;
        this.loginSuccessful = loginSuccessful;
    }
}