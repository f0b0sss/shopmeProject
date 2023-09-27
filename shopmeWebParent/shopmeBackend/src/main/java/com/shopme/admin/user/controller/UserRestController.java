package com.shopme.admin.user.controller;

import com.shopme.admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/check-email")
    public String checkDuplicateEmail(Long id, String email){
        return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}
