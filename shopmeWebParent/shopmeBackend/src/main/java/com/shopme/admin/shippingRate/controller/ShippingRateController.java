package com.shopme.admin.shippingRate.controller;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.country.service.CountryService;
import com.shopme.admin.shippingRate.service.ShippingRateService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ShippingRateAlreadyExistsException;
import com.shopme.common.exception.ShippingRateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ShippingRateController {
    private String defaultRedirectURL = "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";

    @Autowired
    private ShippingRateService shippingRateService;

    @Autowired
    private CountryService countryService;

    @GetMapping("/shipping_rates")
    public String listFirstPage() {
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "shippingRates", moduleUrl = "/shipping_rates") PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum) {
        shippingRateService.listAllByPage(pageNum, helper);
        return "shipping_rates/shipping_rates";
    }

    @GetMapping("/shipping_rates/new")
    public String newRate(Model model) {
        List<Country> listCountries = countryService.findAllByOrderByNameAsc();

        model.addAttribute("rate", new ShippingRate());
        model.addAttribute("listCountries", listCountries);
        model.addAttribute("pageTitle", "New Rate");

        return "shipping_rates/shipping_rate_form";
    }

    @PostMapping("/shipping_rates/save")
    public String saveRate(ShippingRate rate, RedirectAttributes ra) {
        try {
            shippingRateService.save(rate);
            ra.addFlashAttribute("message", "The shipping rate has been saved successfully.");
        } catch (ShippingRateAlreadyExistsException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/edit/{id}")
    public String editRate(@PathVariable Long id,
                           Model model, RedirectAttributes ra) {
        try {
            ShippingRate rate = shippingRateService.get(id);
            List<Country> listCountries = countryService.findAllByOrderByNameAsc();

            model.addAttribute("listCountries", listCountries);
            model.addAttribute("rate", rate);
            model.addAttribute("pageTitle", "Edit Rate (ID: " + id + ")");

            return "shipping_rates/shipping_rate_form";
        } catch (ShippingRateNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }
    }

    @GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
    public String updateCODSupport(@PathVariable Long id,
                                   @PathVariable Boolean supported,
                                   RedirectAttributes ra) {
        try {
            shippingRateService.updateCODSupport(id, supported);
            ra.addFlashAttribute("message", "COD support for shipping rate ID " + id + " has been updated.");
        } catch (ShippingRateNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/delete/{id}")
    public String deleteRate(@PathVariable Long id, RedirectAttributes ra) {
        try {
            shippingRateService.deleteById(id);
            ra.addFlashAttribute("message", "The shipping rate ID " + id + " has been deleted.");
        } catch (ShippingRateNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return defaultRedirectURL;
    }
}
