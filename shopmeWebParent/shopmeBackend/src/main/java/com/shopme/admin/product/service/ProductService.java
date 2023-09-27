package com.shopme.admin.product.service;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<Product> listAll();

    Page<Product> listAllByPage(int pageNum, String sortField, String sortDir, String keyword, Long categoryId);

    Product get(Long id) throws ProductNotFoundException;

    Product save(Product product);

    void deleteById(Long id) throws ProductNotFoundException;

    String checkUnique(Long id, String name);

    void saveProductPrice(Product productInForm);

    void updateEnabledStatus(Long id, boolean enabled);

}