package com.shopme.setting.repository;

import com.shopme.common.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    List<Country> findAllByOrderByNameAsc();

    Country findByCode(String code);

}
