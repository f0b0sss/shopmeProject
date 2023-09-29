package com.shopme.admin.customer.repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends SearchRepository<Customer, Long> {
    Customer findByEmail(String email);

    @Query("select c from Customer c where concat(c.email, ' ', c.firstname, ' ', c.lastname, ' ', " +
            "c.addressLine1, ' ', c.addressLine2, ' ', c.city, ' ', c.state, ' ', c.postalCode, ' ', " +
            "c.country.name) like %?1% ")
    Page<Customer> findAllByKeyword(String keyword, Pageable pageable);

    Long countById(Long id);

    @Query("update Customer c set c.enabled = ?2 where c.id = ?1")
    @Modifying
    @Transactional
    void updateEnabledStatus(Long id, boolean enabled);
}
