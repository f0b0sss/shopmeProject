package com.shopme.admin.user.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.admin.user.service.UserService;
import com.shopme.admin.user.service.export.UserCsvExporter;
import com.shopme.admin.user.service.export.UserExcelExporter;
import com.shopme.admin.user.service.export.UserPDFExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listAllFirstPage() {
        return "redirect:/users/page/1?sortField=firstname&sortDir=asc";
    }

    @GetMapping("/page/{pageNum}")
    public String listAllByPage(@PagingAndSortingParam(listName = "listUsers", moduleUrl = "/users") PagingAndSortingHelper helper,
                                @PathVariable int pageNum) {
        userService.listAllByPage(pageNum, helper);

        return "users/users";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        List<Role> listRoles = userService.listRoles();

        User user = new User();
        user.setEnabled(true);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");

        return "users/user-form";
    }

    @PostMapping("/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam MultipartFile image) throws IOException {

        if (!image.isEmpty()){
            String filename = StringUtils.cleanPath(image.getOriginalFilename());

            user.setPhotos(filename);
            User savedUser = userService.save(user);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, image);
        }else {
            if (user.getPhotos().isEmpty()){
                user.setPhotos(null);
            }

            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

        return getRedirectUrlToAffectedUser(user);
    }

    private String getRedirectUrlToAffectedUser(User user) {
        String userEmail = user.getEmail();
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + userEmail;
    }

    @GetMapping("/{id}")
    public String editUser(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("pageTitle", "Edit User");
            return "users/user-form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteById(id);

            String imagesDir = "user-photos/" + id;
            FileUploadUtil.removeDir(imagesDir);

            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/users";
    }

    @GetMapping("/{id}/enabled/{enabled}")
    public String updateUserEnabledStatus(@PathVariable Long id,
                                          @PathVariable boolean enabled,
                                          RedirectAttributes redirectAttributes){

        userService.updateEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";

        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> users = userService.listAll();
        UserCsvExporter exporter = new UserCsvExporter();

        exporter.export(users, response);
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> users = userService.listAll();

        UserExcelExporter exporter = new UserExcelExporter();

        exporter.export(users, response);
    }

    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        List<User> users = userService.listAll();

        UserPDFExporter exporter = new UserPDFExporter();

        exporter.export(users, response);
    }
}
