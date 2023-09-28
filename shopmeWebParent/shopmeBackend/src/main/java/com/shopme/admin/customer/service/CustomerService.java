package com.shopme.admin.customer.service;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Page<Customer> listAllByPage(int pageNum, String sortField, String sortDir, String keyword);

    void updateEnabledStatus(Long id, boolean enabled);

    Customer findByEmail(String email);

    boolean isEmailUnique(Long id, String email);

    void save(Customer customerInForm);

    void deleteById(Long id) throws CustomerNotFoundException;

    Customer get(Long id) throws CustomerNotFoundException;
}
