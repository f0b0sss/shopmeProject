package com.shopme.admin.setting.setting.service;

import com.shopme.common.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    List<Currency> allCurrenciesOrderByName();

    Optional<Currency> findById(Integer id);
}
