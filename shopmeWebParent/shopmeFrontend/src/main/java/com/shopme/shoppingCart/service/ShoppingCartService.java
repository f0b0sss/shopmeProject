package com.shopme.shoppingCart.service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ShoppingCartException;

import java.util.List;

public interface ShoppingCartService {
    Integer addProduct(Long productId, Integer quantity, Customer customer) throws ShoppingCartException;

    List<CartItem> cartItemList(Customer customer);

    float updateQuantity(Long productId, Integer quantity, Customer customer);

    void removeProduct(Long productId, Customer customer);

    void  deleteByCustomer(Customer customer);
}
