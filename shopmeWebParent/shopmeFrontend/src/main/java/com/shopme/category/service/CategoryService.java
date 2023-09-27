package com.shopme.category.service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {

    List<Category> findAllEnabled();

    List<Category> allNoChildrenCategories();

    List<Category> getCategoryParents(Category child);

    Category getCategoryByAlias(String alias) throws CategoryNotFoundException;

}
