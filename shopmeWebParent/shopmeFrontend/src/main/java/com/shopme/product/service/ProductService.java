package com.shopme.product.service;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<Product> listByCategory(int pageNum, Long categoryId);

    Product findByAlias(String alias) throws ProductNotFoundException;

    Page<Product> search(String keyword, int pageNum);

}
