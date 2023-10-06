package com.shopme.ckeckout;

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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    @Autowired
    private CheckoutService checkoutService;
//    @Autowired private ControllerHelper controllerHelper;
    @Autowired private AddressService addressService;
    @Autowired private ShippingRateService shippingRateService;
    @Autowired private ShoppingCartService shoppingCartService;
    @Autowired private CustomerService customerService;
//    @Autowired private OrderService orderService;
//    @Autowired private SettingService settingService;
//    @Autowired private PayPalService paypalService;

    @GetMapping
    public String showCheckoutPage(Model model, HttpServletRequest request){
        Customer customer = getAuthenticatedCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;

        if (defaultAddress != null){
            model.addAttribute("shippingAddress", defaultAddress.toString());
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else {
            model.addAttribute("shippingAddress", customer.toString());
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }

        if (shippingRate == null){
            return "redirect:/cart";
        }

        List<CartItem> cartItems = shoppingCartService.cartItemList(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);

        return "checkout/checkout";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getAuthenticatedCustomer(request);

        return customerService.findByEmail(email);
    }
}
