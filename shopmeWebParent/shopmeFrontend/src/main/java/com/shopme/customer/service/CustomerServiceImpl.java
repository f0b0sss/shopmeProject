package com.shopme.customer.service;

import com.shopme.common.entity.Customer;
import com.shopme.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer findByVerificationCode(String code) {
        return customerRepository.findByVerificationCode(code);
    }

    @Override
    public void enable(Long id) {
        customerRepository.enable(id);
    }

    @Override
    public boolean isEmailUnique(String email){
        Customer customer = customerRepository.findByEmail(email);

        return customer == null;
    }

    @Override
    public void registerCustomer(Customer customer) {
        encodePassword(customer);

        customer.setEnabled(false);
        customer.setCreatedTime(new Date());

        String randomCode = UUID.randomUUID().toString();
        customer.setVerificationCode(randomCode);

        customerRepository.save(customer);
    }

    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());

        customer.setPassword(encodedPassword);
    }
}
