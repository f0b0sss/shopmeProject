package com.shopme.admin.user.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.service.UserService;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("account")
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping()
    private String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        model.addAttribute("user", user);

        return "users/account-form";
    }

    @PostMapping("/update")
    private String saveDetails(User user, RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                               @RequestParam MultipartFile image) throws IOException {
        if (!image.isEmpty()){
            String filename = StringUtils.cleanPath(image.getOriginalFilename());

            user.setPhotos(filename);
            User savedUser = userService.updateAccount(user);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, image);
        }else {
            if (user.getPhotos().isEmpty()){
                user.setPhotos(null);
            }

            userService.updateAccount(user);
        }

        loggedUser.setFirstname(user.getFirstname());
        loggedUser.setLastname(user.getLastname());

        redirectAttributes.addFlashAttribute("message", "Your account details have been updated successfully.");

        return "redirect:/account";
    }

}
