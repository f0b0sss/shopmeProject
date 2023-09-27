package com.shopme.admin.setting.setting.service;

import com.shopme.admin.setting.setting.repository.CurrencyRepository;
import com.shopme.common.entity.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService{

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public List<Currency> allCurrenciesOrderByName(){
        return currencyRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Optional<Currency> findById(Integer id) {
        return currencyRepository.findById(id);
    }
}
