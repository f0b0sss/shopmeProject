package com.shopme.admin.product.service;

import com.shopme.admin.product.repository.ProductRepository;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {
    public static final int PRODUCTS_PER_PAGE = 2;

    @Autowired
    private ProductRepository repository;

    @Override
    public List<Product> listAll() {
        return repository.findAll();
    }

    @Override
    public Page<Product> listAllByPage(int pageNum, String sortField, String sortDir, String keyword,
                                       Long categoryId) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0){
                String categoryIdMatch = "-" + categoryId + "-";
                return repository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            }

            return repository.findAllByKeyword(keyword, pageable);
        }

        if (categoryId != null && categoryId > 0){
            String categoryIdMatch = "-" + categoryId + "-";
            return repository.findAllInCategory(categoryId, categoryIdMatch, pageable);
        }

        return repository.findAll(pageable);
    }

    @Override
    public Product get(Long id) throws ProductNotFoundException {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().toLowerCase().replace(" ", "_");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().toLowerCase().replace(" ", "_"));
        }

        product.setUpdatedTime(new Date());

        return repository.save(product);
    }

    @Override
    public void deleteById(Long id) throws ProductNotFoundException {
        Long countById = repository.countById(id);

        if (countById == null || countById == 0) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }

        repository.deleteById(id);
    }

    @Override
    public String checkUnique(Long id, String name) {
        boolean isCreatingNew = (id == null || id == 0);

        Product productByName = repository.findByName(name);

        if (isCreatingNew) {
            if (productByName != null) {
                return "Duplicate";
            } else {
                if (productByName != null && productByName.getId() != id) {
                    return "Duplicate";
                }
            }
        }

        return "OK";
    }

    @Override
    public void saveProductPrice(Product productInForm) {
        Product productInDB = repository.findById(productInForm.getId()).get();
        productInDB.setCost(productInForm.getCost());
        productInDB.setPrice(productInForm.getPrice());
        productInDB.setDiscountPercent(productInForm.getDiscountPercent());

        repository.save(productInDB);
    }

    @Override
    @Transactional
    public void updateEnabledStatus(Long id, boolean enabled) {
        repository.updateEnabledStatus(id, enabled);
    }
}
