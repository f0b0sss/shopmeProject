package com.shopme.customer.service;

import com.shopme.common.entity.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    List<Country> findAllByOrderByNameAsc();

    Country save(Country country);

    Optional<Country> findById(Integer countryId);

    void deleteById(Integer id);
}
