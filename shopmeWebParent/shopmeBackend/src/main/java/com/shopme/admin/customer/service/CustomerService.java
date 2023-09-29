package com.shopme.admin.customer.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

public interface CustomerService {
    void listAllByPage(int pageNum, PagingAndSortingHelper helper);

    void updateEnabledStatus(Long id, boolean enabled);

    Customer findByEmail(String email);

    boolean isEmailUnique(Long id, String email);

    void save(Customer customerInForm);

    void deleteById(Long id) throws CustomerNotFoundException;

    Customer get(Long id) throws CustomerNotFoundException;
}
