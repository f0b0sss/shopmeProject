package com.shopme.admin.customer.controller;

import com.shopme.admin.customer.service.CustomerService;
import com.shopme.admin.customer.service.CustomerServiceImpl;
import com.shopme.admin.setting.country.service.CountryService;
import com.shopme.admin.setting.setting.service.SettingService;
import com.shopme.common.entity.*;
import com.shopme.common.exception.CustomerNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private SettingService settingService;

    @GetMapping()
    public String listFirstPage(Model model) {
        return listAllByPage(1, "firstname", "asc", null, model);
    }


    @GetMapping("/page/{pageNum}")
    public String listAllByPage(@PathVariable int pageNum,
                                @Param("sortField") String sortField,
                                @Param("sortDir") String sortDir,
                                @Param("keyword") String keyword,
                                Model model) {
        Page<Customer> page = customerService.listAllByPage(pageNum, sortField, sortDir, keyword);
        List<Customer> customerList = page.getContent();

        long startCount = (long) (pageNum - 1) * CustomerServiceImpl.CUSTOMERS_PER_PAGE + 1;
        long endCount = startCount + CustomerServiceImpl.CUSTOMERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("customerList", customerList);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortField", "firstname");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleUrl", "/customers");

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