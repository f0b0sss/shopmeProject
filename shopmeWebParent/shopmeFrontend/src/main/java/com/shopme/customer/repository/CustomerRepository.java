package com.shopme.customer.repository;

import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);

    Customer findByVerificationCode(String code);

    @Query("update Customer c set c.enabled = true where c.id = ?1")
    @Modifying
    void enable(Long id);
}
