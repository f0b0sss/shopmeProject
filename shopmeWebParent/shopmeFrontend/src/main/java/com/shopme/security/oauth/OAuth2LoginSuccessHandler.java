package com.shopme.security.oauth;

import com.shopme.common.entity.enums.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.customer.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Lazy
    @Autowired
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomerOAuth2User oAuth2User = (CustomerOAuth2User) authentication.getPrincipal();

        String name = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String clientName = oAuth2User.getClientName();
        String countryCode = request.getLocale().getCountry();

        Customer customer = customerService.findByEmail(email);

        AuthenticationType authenticationType = getAuthenticationType(clientName);

        if (customer == null){
            customerService.addNewCustomerUponOAuthLogin(name, email, countryCode, authenticationType);
        }else {
            oAuth2User.setFullName(customer.getFullName());
            customerService.updateAuthenticationType(customer, authenticationType);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(String clientName){
        return switch (clientName) {
            case "Google" -> AuthenticationType.GOOGLE;
            case "Facebook" -> AuthenticationType.FACEBOOK;
            default -> AuthenticationType.DATABASE;
        };
    }
}
