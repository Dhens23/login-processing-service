package com.assessment.login_processing_service.infrastructure.out.db.repository;

import com.assessment.login_processing_service.infrastructure.out.db.model.CustomerLoginResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerLoginResultRepository extends JpaRepository<CustomerLoginResult, UUID> {
}