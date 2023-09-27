package com.shopme.admin.category.service;

import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.admin.category.utils.CategoryPageInfo;
import com.shopme.common.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    List<Category> listCategoriesUsedInForm();
    Category save(Category category);

    Page<Category> listAllByPage(int pageNum, String sortField, String sortDir, String keyword);
    List<Category> listAllByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir, String keyword);

    Category updateAccount(Category categoryInForm);

    Category getCategoryById(Long id) throws CategoryNotFoundException;

    void deleteById(Long id) throws CategoryNotFoundException;

    void updateEnabledStatus(Long id, boolean enabled);

    String checkUnique(Long id, String name, String alias);
}
