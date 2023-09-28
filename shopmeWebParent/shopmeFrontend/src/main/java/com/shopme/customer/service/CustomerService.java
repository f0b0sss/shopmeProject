package com.shopme.customer.service;

import com.shopme.common.entity.Customer;

public interface CustomerService {
    Customer findByEmail(String email);

    Customer findByVerificationCode(String code);

    void enable(Long id);

    boolean isEmailUnique(String email);

    void registerCustomer(Customer customer);

    boolean verify(String verificationCode);
}
