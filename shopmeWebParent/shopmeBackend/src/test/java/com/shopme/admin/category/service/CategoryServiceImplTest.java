package com.shopme.admin.category.service;

import com.shopme.admin.category.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class CategoryServiceImplTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService = new CategoryServiceImpl();

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateName() {
        Long id = null;
        String name = "Computers";
        String alias = "abc";

        Category category = new Category(id, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryService.checkUnique(id, name, alias);

        assertEquals("DuplicateName", result);
    }

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateAlias() {
        Long id = null;
        String name = "NameABC";
        String alias = "Computers";

        Category category = new Category(id, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUnique(id, name, alias);

        assertEquals("DuplicateAlias", result);
    }

    @Test
    public void testCheckUniqueInNewModeReturnOk() {
        Long id = null;
        String name = "NameABC";
        String alias = "Computers";

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryService.checkUnique(id, name, alias);

        assertEquals("OK", result);
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateName() {
        Long id = 1L;
        String name = "Computers";
        String alias = "abc";

        Category category = new Category(2L, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUnique(id, name, alias);

        assertEquals("DuplicateName", result);
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateAlias() {
        Long id = 1L;
        String name = "NameABC";
        String alias = "Computers";

        Category category = new Category(2L, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUnique(id, name, alias);

        assertEquals("DuplicateAlias", result);
    }

    @Test
    public void testCheckUniqueInEditModeReturnOk() {
        Long id = 1L;
        String name = "NameABC";
        String alias = "Computers";

        Category category = new Category(id, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUnique(id, name, alias);

        assertEquals("OK", result);
    }


}