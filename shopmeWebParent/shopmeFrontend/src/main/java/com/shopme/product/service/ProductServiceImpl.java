package com.shopme.product.service;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{
    public static final int PRODUCTS_PER_PAGE = 2;
    public static final int SEARCH_RESULT_PER_PAGE = 2;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> listByCategory(int pageNum, Long categoryId) {
        String categoryIdMatch = "-" + categoryId + "-";
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
    }

    @Override
    public Product findByAlias(String alias) throws ProductNotFoundException {
        Product product = productRepository.findByAlias(alias);

        if (product == null){
            throw new ProductNotFoundException("Could not find any product with alias " + alias);
        }

        return product;
    }

    @Override
    public Page<Product> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULT_PER_PAGE);

        return productRepository.search(keyword, pageable);
    }
}
