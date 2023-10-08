package com.shopme.admin.product.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {
    List<Product> listAll();

    void listAllByPage(int pageNum, PagingAndSortingHelper helper, Long categoryId);

    void searchProducts(int pageNum, PagingAndSortingHelper helper);

    Product get(Long id) throws ProductNotFoundException;

    Product save(Product product);

    void deleteById(Long id) throws ProductNotFoundException;

    String checkUnique(Long id, String name);

    void saveProductPrice(Product productInForm);

    void updateEnabledStatus(Long id, boolean enabled);

}