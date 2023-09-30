package com.shopme.customer.controller;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(){


        return "customer/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPasswordForm(HttpServletRequest request, Model model){
        String email = request.getParameter("email");

        try {
            String token = customerService.updateResetPasswordToken(email);
            String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(link, email);
            model.addAttribute("message","We have sent a reset password link to the email. Please check.");
        } catch (CustomerNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", "Could not send Email");
        }

        return "customer/forgot_password_form";
    }

    private void sendEmail(String link, String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = email;
        String subject = "Link to reset password";

        String content = "<p>Hello, </p>\n" +
                "    <p>You have requested to reset your password.</p>\n" +
                "    <p>Click the link  below to change the password</p>\n" +
                "    <p>\n" +
                "        <a href=\"" + link + "\">Change my password</a></p>" +
                "<br>" +
                "<p>Ignore this email is you do remember or you have not made the request </p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getUsername(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token") String token, Model model){
        Customer customer = customerService.getByResetPasswordToken(token);

        if (customer != null){
            model.addAttribute("token",token);
        }else {
            model.addAttribute("pageTitle","Invalid token");
            model.addAttribute("message","Invalid token");
            return "message";
        }

        return "customer/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetForm(Model model, HttpServletRequest request){
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
            customerService.updatePassword(token, password);

            model.addAttribute("pageTitle", "Reset Password");
            model.addAttribute("title", "Reset Your Password");
            model.addAttribute("message", "You have successfully changed password");

            return "message";
        } catch (CustomerNotFoundException e) {
            model.addAttribute("pageTitle","Invalid token");
            model.addAttribute("message",e.getMessage());

            return "message";
        }
    }
}
