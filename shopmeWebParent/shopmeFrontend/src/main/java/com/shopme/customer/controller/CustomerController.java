package com.shopme.customer.controller;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.security.CustomerUserDetails;
import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.service.CountryService;
import com.shopme.customer.service.CustomerService;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.service.SettingService;
import com.shopme.utility.Utility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String showRegisteredForm(Model model) {
        List<Country> countryList = countryService.findAllByOrderByNameAsc();

        model.addAttribute("countryList", countryList);
        model.addAttribute("pageTitle", "Customer Registration");
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }

    @PostMapping("/create_customer")
    public String crateCustomer(Customer customer, Model model, HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(request, customer);

        model.addAttribute("pageTitle", "Registration Succeeded");

        return "register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getUsername(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFullName());

        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model) {
        boolean verified = customerService.verify(code);

        return "register/" + (verified ? "verify_success" : "verify_fail");
    }

    @GetMapping("/account_details")
    public String accountDetails(Model model, HttpServletRequest request) {
        String email = getAuthenticatedCustomer(request);
        Customer customer = customerService.findByEmail(email);
        List<Country> countryList = countryService.findAllByOrderByNameAsc();

        model.addAttribute("pageTitle", "Your Account Details");
        model.addAttribute("customer", customer);
        model.addAttribute("countryList", countryList);

        return "customer/account_form";
    }

    private String getAuthenticatedCustomer(HttpServletRequest request) {
        Object principal = request.getUserPrincipal();
        String customerEmail = null;

        if (principal instanceof UsernamePasswordAuthenticationToken ||
                principal instanceof RememberMeAuthenticationToken) {
            customerEmail = request.getUserPrincipal().getName();
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            customerEmail = oAuth2User.getEmail();
        }

        return customerEmail;
    }

    @PostMapping("/update_account_details")
    public String updateAccountDetails(Customer customer, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        customerService.update(customer);

        updateNameForAuthenticatedCustomer(customer, request);

        redirectAttributes.addFlashAttribute("message", "Your account details haves been updated.");

        return "redirect:/account_details";
    }

    private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
        Object principal = request.getUserPrincipal();

        if (principal instanceof UsernamePasswordAuthenticationToken ||
                principal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetails userDetails = getCustomerUserDetails(principal);

            userDetails.getCustomer().setFirstname(customer.getFirstname());
            userDetails.getCustomer().setLastname(customer.getLastname());

        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();

            String fullName = customer.getFirstname() + " " + customer.getLastname();
            oAuth2User.setFullName(fullName);
        }
    }

    private CustomerUserDetails getCustomerUserDetails(Object principal){
        CustomerUserDetails userDetails = null;
        if (principal instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            userDetails = (CustomerUserDetails) token.getPrincipal();
        }else if (principal instanceof RememberMeAuthenticationToken){
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
            userDetails = (CustomerUserDetails) token.getPrincipal();
        }

        return userDetails;
    }

}
