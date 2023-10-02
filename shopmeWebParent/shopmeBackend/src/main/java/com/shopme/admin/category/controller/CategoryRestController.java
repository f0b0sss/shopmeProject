package com.shopme.admin.category.controller;

import com.shopme.admin.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories/check-unique")
    public String checkUnique(Long id, String name, String alias) {
        return categoryService.checkUnique(id, name, alias);
    }
}
