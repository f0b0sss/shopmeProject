package com.shopme.admin.setting.country.repository;

import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;


    @Test
    public void countries(){
        List<Country> countries = countryRepository.findAllByOrderByNameAsc();
        System.out.println(countries);
    }

    @Test
    public void createCountry(){
        countryRepository.save(new Country("China", "CN"));
        countryRepository.save(new Country("Ukraine", "UK"));
        countryRepository.save(new Country("UAS", "US"));
    }

}