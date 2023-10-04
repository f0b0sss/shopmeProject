package com.shopme.address.controller;

import com.shopme.address.service.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.customer.service.CustomerService;
import com.shopme.setting.service.CountryService;
import com.shopme.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CountryService countryService;


    @GetMapping("/address_book")
    public String showAddressBook(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        List<Address> addressList = addressService.listAddressBook(customer);

        boolean usePrimaryAddressAsDefault = true;

        for (Address address : addressList) {
            if (address.isDefaultForShipping()) {
                usePrimaryAddressAsDefault = false;
                break;
            }
        }

        model.addAttribute("customer", customer);
        model.addAttribute("addressList", addressList);
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);

        return "address_book/addresses";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getAuthenticatedCustomer(request);

        return customerService.findByEmail(email);
    }

    @GetMapping("/address_book/new")
    public String newAddress(Model model) {
        List<Country> listCountries = countryService.findAllByOrderByNameAsc();

        model.addAttribute("listCountries", listCountries);
        model.addAttribute("address", new Address());
        model.addAttribute("pageTitle", "Add New Address");

        return "address_book/address_form";
    }

    @PostMapping("/address_book/save")
    public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra) {
        Customer customer = getAuthenticatedCustomer(request);

        address.setCustomer(customer);
        addressService.save(address);

//        String redirectOption = request.getParameter("redirect");
//        String redirectURL = "redirect:/address_book";
//
//        if ("checkout".equals(redirectOption)) {
//            redirectURL += "?redirect=checkout";
//        }

        ra.addFlashAttribute("message", "The address has been saved successfully.");

        return "redirect:/address_book";
    }

    @GetMapping("/address_book/edit/{addressId}")
    public String editAddress(@PathVariable Long addressId, Model model,
                              HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        List<Country> listCountries = countryService.findAllByOrderByNameAsc();

        Address address = addressService.get(addressId, customer.getId());

        model.addAttribute("address", address);
        model.addAttribute("listCountries", listCountries);
        model.addAttribute("pageTitle", "Edit Address (ID: " + addressId + ")");

        return "address_book/address_form";
    }

    @GetMapping("/address_book/delete/{addressId}")
    public String deleteAddress(@PathVariable Long addressId, RedirectAttributes ra,
                                HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        addressService.delete(addressId, customer.getId());

        ra.addFlashAttribute("message", "The address ID " + addressId + " has been deleted.");

        return "redirect:/address_book";
    }

    @GetMapping("/address_book/default/{addressId}")
    public String setDefault(@PathVariable Long addressId,
                                HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        addressService.setDefaultAddress(addressId, customer.getId());

        return "redirect:/address_book";
    }
}
