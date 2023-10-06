package com.shopme.shoppingCart.controller;

import com.shopme.address.service.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.customer.service.CustomerService;
import com.shopme.shipping.service.ShippingRateService;
import com.shopme.shoppingCart.service.ShoppingCartService;
import com.shopme.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ShippingRateService shippingRateService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request){
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = shoppingCartService.cartItemList(customer);

        float estimatedTotal = 0.0F;
        for (CartItem cartItem : cartItems) {
            estimatedTotal += cartItem.getSubTotal();
        }

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;
        boolean usePrimaryAddressAsDefault = false;
        if (defaultAddress != null){
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else {
            usePrimaryAddressAsDefault = true;
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }


        model.addAttribute("shippingSupported", shippingRate != null);
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("estimatedTotal", estimatedTotal);

        return "cart/shopping_cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getAuthenticatedCustomer(request);

        return customerService.findByEmail(email);
    }

}
