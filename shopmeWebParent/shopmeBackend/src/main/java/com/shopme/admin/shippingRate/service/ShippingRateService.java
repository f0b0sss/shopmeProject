package com.shopme.admin.shippingRate.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ShippingRateAlreadyExistsException;
import com.shopme.common.exception.ShippingRateNotFoundException;

public interface ShippingRateService {
    void listAllByPage(int pageNum, PagingAndSortingHelper helper);

    ShippingRate get(Long id) throws ShippingRateNotFoundException;

    ShippingRate save(ShippingRate shippingRate) throws ShippingRateAlreadyExistsException;

    void deleteById(Long id) throws ShippingRateNotFoundException;

    void updateCODSupport(Long id, boolean codSupported) throws ShippingRateNotFoundException;

    float calculateShippingCost(Long productId, Integer countryId, String state)
            throws ShippingRateNotFoundException;
}
