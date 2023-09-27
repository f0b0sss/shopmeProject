package com.shopme.category.service;

import com.shopme.category.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
//    public static final int ROOT_CATEGORIES_PER_PAGE = 1;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAllEnabled() {
        return null;
    }

    @Override
    public List<Category> allNoChildrenCategories() {
        List<Category> listNoChildrenCategories = new ArrayList<>();

        List<Category> listEnabledCategories = categoryRepository.findAllEnabled();

        listEnabledCategories.forEach(category -> {
            Set<Category> children = category.getChildren();
            if (children == null || children.size() == 0){
                listNoChildrenCategories.add(category);
            }
        });

        return listNoChildrenCategories;
    }

    @Override
    public Category getCategoryByAlias(String alias) throws CategoryNotFoundException {
        Category category = categoryRepository.findAllByAliasEnabled(alias);

        if (category == null){
            throw new CategoryNotFoundException("Could not find any category with alias " + alias);
        }

        return category;
    }

    @Override
    public List<Category> getCategoryParents(Category child) {
        List<Category> listParents = new ArrayList<>();

        Category parent = child.getParent();

        while (parent != null){
            listParents.add(parent);
            parent = parent.getParent();
        }

        listParents.add(child);


        return listParents;
    }
}
