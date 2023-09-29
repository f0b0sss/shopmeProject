package com.shopme.admin.brand.service;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> listAll();

    void listAllByPage(int pageNum, PagingAndSortingHelper helper);

    Brand get(Long id) throws BrandNotFoundException;

    Brand save(Brand brand);

    void deleteById(Long id) throws BrandNotFoundException;

    String checkUnique(Long id, String name);
}
