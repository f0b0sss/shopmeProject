package com.shopme.admin.brand.service;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<Brand> listAllByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return repository.findAllByKeyword(keyword, pageable);
        }

        return repository.findAll(pageable);
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
