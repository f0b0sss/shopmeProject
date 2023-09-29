package com.shopme.admin.customer.service;

import com.shopme.admin.customer.repository.CustomerRepository;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CustomerServiceImpl implements CustomerService {
    public static final int CUSTOMERS_PER_PAGE = 2;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void listAllByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, customerRepository);
    }

    @Override
    public void updateEnabledStatus(Long id, boolean enabled){
        customerRepository.updateEnabledStatus(id, enabled);
    }

    public Customer get(Long id) throws CustomerNotFoundException {
        try {
            return customerRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new CustomerNotFoundException("Could not find any customer with ID " + id);
        }
    }


    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public boolean isEmailUnique(Long id, String email) {
        Customer existCustomer = customerRepository.findByEmail(email);

        if (existCustomer != null && existCustomer.getId() != id){
            return false;
        }

        return true;
    }

    @Override
    public void save(Customer customerInForm){
        Customer customerInDB = customerRepository.findById(customerInForm.getId()).get();

        if (customerInForm.getPassword().isEmpty()){
            String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
            customerInForm.setPassword(encodedPassword);
        }else {
            customerInForm.setPassword(customerInDB.getPassword());
        }

        customerInForm.setEnabled(customerInDB.isEnabled());
        customerInForm.setCreatedTime(customerInDB.getCreatedTime());
        customerInForm.setVerificationCode(customerInDB.getVerificationCode());

        customerRepository.save(customerInForm);
    }

    @Override
    public void deleteById(Long id) throws CustomerNotFoundException {
        Long countById = customerRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new CustomerNotFoundException("Could not find any customer with ID " + id);
        }

        customerRepository.deleteById(id);
    }

}
