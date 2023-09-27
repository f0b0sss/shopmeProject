package com.shopme.admin.setting.country.service;

import com.shopme.admin.setting.country.repository.CountryRepository;
import com.shopme.common.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService{
    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<Country> findAllByOrderByNameAsc() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Country save(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Optional<Country> findById(Integer countryId) {
        return countryRepository.findById(countryId);
    }

    @Override
    public void deleteById(Integer id) {
        countryRepository.deleteById(id);
    }
}
