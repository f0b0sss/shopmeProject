package com.shopme.admin.customer.controller;

import com.shopme.admin.customer.service.CustomerService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.country.service.CountryService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CountryService countryService;

    @GetMapping
    public String listAllFirstPage() {
        return "redirect:/customers/page/1?sortField=firstname&sortDir=asc";
    }

    @GetMapping("/page/{pageNum}")
    public String listAllByPage(@PagingAndSortingParam(listName = "customerList", moduleUrl = "/customers") PagingAndSortingHelper helper,
                                @PathVariable int pageNum) {
        customerService.listAllByPage(pageNum, helper);

        return "customers/customers";
    }

    @GetMapping("/{id}/enabled/{enabled}")
    public String updateCustomerEnabledStatus(@PathVariable Long id,
                                             @PathVariable boolean enabled,
                                             RedirectAttributes redirectAttributes) {

        customerService.updateEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";

        String message = "The customer ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/customers";
    }

    @GetMapping("/detail/{id}")
    public String viewCustomer(@PathVariable Long id,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.get(id);
            List<Country> countryList = countryService.findAllByOrderByNameAsc();

            model.addAttribute("customer", customer);
            model.addAttribute("countryList", countryList);

            return "customers/customer-detail-modal";
        }catch (CustomerNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("/{id}")
    public String editCustomer(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.get(id);
            List<Country> countryList = countryService.findAllByOrderByNameAsc();

            model.addAttribute("customer", customer);
            model.addAttribute("countryList", countryList);
            model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");

            return "customers/customer-form";
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/customers";
        }
    }

    @PostMapping("/save")
    public String saveCustomer(Customer customer,
                              Model model,
                              RedirectAttributes redirectAttributes)  {
        customerService.save(customer);

        redirectAttributes.addFlashAttribute("message", "The customer has been saved successfully.");

        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The customer ID " + id + " has been deleted successfully");
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/customers";
    }

}
