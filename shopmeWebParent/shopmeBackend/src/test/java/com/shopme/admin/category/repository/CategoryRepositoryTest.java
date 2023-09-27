package com.shopme.admin.category.repository;

import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    private Category category;

    @Test
    public void createRootCategoryTest(){
        Category category = new Category("Electronics");

        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void createSubCategoryTest(){
        Category parent = new Category(5l);
        Category memory = new Category("Memory", parent);

        categoryRepository.save(memory);
    }

    @Test
    public void getCategoryByIdTest(){
        Category category = categoryRepository.findById(1l).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();
        for (Category child : children) {
            System.out.println(child.getName());
        }

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void printHierarchicalCategoriesTest(){
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            if (category.getParent() == null){
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }

    private void printChildren(Category parent, int subLevel){
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }
            System.out.println(subCategory.getName());

            printChildren(subCategory, newSubLevel);
        }
    }

    @Test
    public void testListRootCategoriesTest(){
        List<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
        rootCategories.forEach(cat -> System.out.println(cat.getName()));
    }

    @Test
    public void findByNameTest(){
        String name = "Computers";
        Category category = categoryRepository.findByName(name);

        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(name);
    }


}