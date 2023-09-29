package com.shopme.admin.brand.service;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandServiceImpl implements BrandService{
    public static final int BRANDS_PER_PAGE = 2;

    @Autowired
    private BrandRepository repository;

    @Override
    public List<Brand> listAll() {
        return repository.findAll();
    }

    @Override
    public void listAllByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, BRANDS_PER_PAGE, repository);
    }

    @Override
    public Brand get(Long id) throws BrandNotFoundException {
        try{
            return repository.findById(id).get();
        }catch (NoSuchElementException e){
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
    }

    @Override
    public Brand save(Brand brand) {
        return repository.save(brand);
    }

    @Override
    public void deleteById(Long id) throws BrandNotFoundException {
        Long countById = repository.countById(id);

        if (countById == null || countById == 0){
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }

        repository.deleteById(id);
    }

    @Override
    public String checkUnique(Long id, String name) {
        boolean isCreatingNew = (id == null || id == 0);

        Brand brandByName = repository.findByName(name);

        if (isCreatingNew) {
            if (brandByName != null) {
                return "Duplicate";
            } else {
                if (brandByName != null && brandByName.getId() != id) {
                    return "Duplicate";
                }
            }
        }

        return "OK";
    }
}
