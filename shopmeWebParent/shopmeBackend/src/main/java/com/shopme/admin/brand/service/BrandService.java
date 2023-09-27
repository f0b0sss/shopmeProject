package com.shopme.admin.brand.service;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {
    List<Brand> listAll();

    Page<Brand> listAllByPage(int pageNum, String sortField, String sortDir, String keyword);

    Brand get(Long id) throws BrandNotFoundException;

    Brand save(Brand brand);

    void deleteById(Long id) throws BrandNotFoundException;

    String checkUnique(Long id, String name);
}
