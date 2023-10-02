package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.dto.CategoryDTO;
import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.exception.BrandNotFoundRestException;
import com.shopme.admin.brand.service.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {

    @Autowired
    private BrandService service;

    @PostMapping("/brands/check-unique")
    public String checkUnique(Long id, String name) {
        return service.checkUnique(id, name);
    }

    @GetMapping("brands/{brandId}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable Long brandId) throws BrandNotFoundRestException {
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        try {
            Brand brand = service.get(brandId);
            Set<Category> categories = brand.getCategories();
            for (Category category : categories) {
                CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName());
                categoryDTOList.add(categoryDTO);
            }

            return categoryDTOList;

        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundRestException();
        }
    }
}
