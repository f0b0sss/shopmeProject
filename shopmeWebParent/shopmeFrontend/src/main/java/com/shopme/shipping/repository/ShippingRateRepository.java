package com.shopme.shipping.repository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Long> {
    ShippingRate findByCountryAndState(Country country, String state);
}
