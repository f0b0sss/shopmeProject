package com.shopme.customer.controller;

import com.shopme.customer.service.CountryService;
import com.shopme.customer.service.CustomerService;
import com.shopme.setting.state.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private StateService stateService;

    @PostMapping("/check-unique-email")
    public String checkDublicateEmail(@Param("email") String email){
        return customerService.isEmailUnique(email) ? "OK" : "Duplicated";

    }

}
