package com.shopme;

import com.shopme.category.service.CategoryService;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String viewHomePage(Model model){
        List<Category> categoryList = categoryService.allNoChildrenCategories();

        model.addAttribute("categoryList", categoryList);

        return "index";
    }
}
