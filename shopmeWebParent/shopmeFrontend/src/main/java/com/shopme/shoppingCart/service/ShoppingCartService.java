package com.shopme.shoppingCart.service;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ShoppingCartException;

public interface ShoppingCartService {
    Integer addProduct(Long productId, Integer quantity, Customer customer) throws ShoppingCartException;
}
