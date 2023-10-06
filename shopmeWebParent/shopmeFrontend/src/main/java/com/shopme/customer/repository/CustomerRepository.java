package com.shopme.customer.repository;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);

    Customer findByResetPasswordToken(String token);
    Customer findByVerificationCode(String code);

    @Query("update Customer c set c.enabled = true, c.verificationCode = null where c.id = ?1")
    @Modifying
    @Transactional
    void enable(Long id);

    @Query("update Customer c set c.authenticationType = ?2 where c.id = ?1")
    @Modifying
    @Transactional
    void updateAuthenticationType(Long customerId, AuthenticationType type);
}
