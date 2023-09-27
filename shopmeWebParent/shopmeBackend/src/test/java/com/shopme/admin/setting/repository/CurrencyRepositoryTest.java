package com.shopme.admin.setting.repository;

import com.shopme.admin.setting.setting.repository.CurrencyRepository;
import com.shopme.common.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void createCurrency(){
        List<Currency> currencyList = Arrays.asList(
                new Currency("United States Dollar", "$", "USD"),
                new Currency("Euro", "€", "EUR"),
                new Currency("Ukraine Hrivna", "₴", "UAH"),
                new Currency("Russian Ruble States Dollar", "₽", "RUB")
        );

        currencyRepository.saveAll(currencyList);

    }

    @Test
    public void findAllByOrderByNameAsc(){
        List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();

        currencies.forEach(System.out::println);
    }

}