package com.shopme.shoppingCart.controller;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.common.exception.ShoppingCartException;
import com.shopme.customer.service.CustomerService;
import com.shopme.shoppingCart.service.ShoppingCartService;
import com.shopme.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingCartRestController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable Long productId,
                                   @PathVariable Integer quantity,
                                   HttpServletRequest request) {
        try {
            Customer customer = getAuthenticatedCustomer(request);
            Integer updatedQuantity = shoppingCartService.addProduct(productId, quantity, customer);

            return updatedQuantity + " item(s) of this product were added to your shopping cart";
        } catch (CustomerNotFoundException e) {
            return "You must login to add this product to cart.";
        } catch (ShoppingCartException e) {
            return e.getMessage();
        }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getAuthenticatedCustomer(request);

        if (email == null) {
            throw new CustomerNotFoundException("No authenticated customer");
        }

        return customerService.findByEmail(email);
    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable Long productId,
                                   @PathVariable Integer quantity,
                                   HttpServletRequest request) {
        try {
            Customer customer = getAuthenticatedCustomer(request);

            float subtotal = shoppingCartService.updateQuantity(productId, quantity, customer);

            return String.valueOf(subtotal);
        } catch (CustomerNotFoundException e) {
            return "You must login to change quantity of product.";
        }
    }

    @DeleteMapping("/cart/remove/{productId}")
    public String removeProduct(@PathVariable Long productId,
                                 HttpServletRequest request) {
        try {
            Customer customer = getAuthenticatedCustomer(request);

            shoppingCartService.removeProduct(productId, customer);

            return "The product has been removed from your shopping cart";
        } catch (CustomerNotFoundException e) {
            return "You must login to remove product.";
        }

    }
}
