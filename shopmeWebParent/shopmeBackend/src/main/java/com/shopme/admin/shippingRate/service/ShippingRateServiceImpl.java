package com.shopme.admin.shippingRate.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.product.repository.ProductRepository;
import com.shopme.admin.shippingRate.repository.ShippingRateRepository;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ShippingRateAlreadyExistsException;
import com.shopme.common.exception.ShippingRateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ShippingRateServiceImpl implements ShippingRateService {
    public static final int RATES_PER_PAGE = 2;
    private static final int DIM_DIVISOR = 139;

    @Autowired
    private ShippingRateRepository shippingRateRepository;

    @Autowired
    private ProductRepository productRepository;


    @Override
    public void listAllByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, RATES_PER_PAGE, shippingRateRepository);
    }

    @Override
    public ShippingRate get(Long id) throws ShippingRateNotFoundException {
        try {
            return shippingRateRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ShippingRateNotFoundException("Could not find any shipping rate with ID " + id);
        }
    }

    @Override
    public ShippingRate save(ShippingRate shippingRate) throws ShippingRateAlreadyExistsException {
        ShippingRate rateInDB = shippingRateRepository.findByCountryAndState(shippingRate.getCountry().getId(), shippingRate.getState());

        boolean foundExistingRateInNewMode = shippingRate.getId() == null && rateInDB != null;
        boolean foundDifferentExistingRateInEditMode = shippingRate.getId() != null && rateInDB != null && rateInDB.equals(shippingRate);

        if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
            throw new ShippingRateAlreadyExistsException("There's already a rate for the destination "
                    + shippingRate.getCountry().getName() + ", " + shippingRate.getState());
        }

        return shippingRateRepository.save(shippingRate);
    }

    @Override
    public void deleteById(Long id) throws ShippingRateNotFoundException {
        Long countById = shippingRateRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new ShippingRateNotFoundException("Could not find any shipping rate with ID " + id);
        }

        shippingRateRepository.deleteById(id);
    }

    @Override
    public void updateCODSupport(Long id, boolean codSupported) throws ShippingRateNotFoundException {
        Long count = shippingRateRepository.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
        }

        shippingRateRepository.updateCODSupport(id, codSupported);
    }

    @Override
    public float calculateShippingCost(Long productId, Integer countryId, String state)
            throws ShippingRateNotFoundException {
        ShippingRate shippingRate = shippingRateRepository.findByCountryAndState(countryId, state);

        if (shippingRate == null) {
            throw new ShippingRateNotFoundException("No shipping rate found for the given "
                    + "destination. You have to enter shipping cost manually.");
        }

        Product product = productRepository.findById(productId).get();

        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
        float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;

        return finalWeight * shippingRate.getRate();
    }
}
