package com.shopme.admin.customer.controller;

import com.shopme.admin.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customers/check-email")
    public String checkDuplicateEmail(@Param("id") Long id,
                              @Param("name") String name) {
        if (customerService.isEmailUnique(id, name)) {
            return "OK";
        } else {
            return "Duplicated";
        }
    }
}
