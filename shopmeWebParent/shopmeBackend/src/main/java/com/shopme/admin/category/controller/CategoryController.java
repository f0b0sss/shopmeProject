package com.shopme.admin.category.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.admin.category.service.CategoryServiceImpl;
import com.shopme.admin.category.service.export.CategoryCsvExporter;
import com.shopme.admin.category.utils.CategoryPageInfo;
import com.shopme.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listAllFirstPage(@Param("sortDir") String sortDir, Model model) {
        return listAllByPage(1, sortDir, null, model);
    }

    @GetMapping("/page/{pageNum}")
    public String listAllByPage(@PathVariable int pageNum,
                                @Param("sortDir") String sortDir,
                                @Param("keyword") String keyword,
                                Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        CategoryPageInfo pageInfo = new CategoryPageInfo();

        List<Category> listCategories = categoryService.listAllByPage(pageInfo, pageNum, sortDir, keyword);

        long startCount = (long) (pageNum - 1) * CategoryServiceImpl.ROOT_CATEGORIES_PER_PAGE + 1;
        long endCount = startCount + CategoryServiceImpl.ROOT_CATEGORIES_PER_PAGE - 1;
        if (endCount > pageInfo.getTotalElements()) {
            endCount = pageInfo.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleUrl", "/categories");

        return "categories/categories";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        Category category = new Category();
        category.setEnabled(true);

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("category", category);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");

        return "categories/category-form";
    }

    @PostMapping("/save")
    public String saveCategory(Category category, RedirectAttributes redirectAttributes, @RequestParam MultipartFile fileImage) throws IOException {

        if (!fileImage.isEmpty()) {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(fileImage.getOriginalFilename()));

            category.setImage(filename);

            Category savedCategory = categoryService.save(category);

            String uploadDir = "category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, fileImage);
        } else {
            if (category.getImage().isEmpty()) {
                category.setImage(null);
            }

            categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");

        return getRedirectUrlToAffectedCategory(category);
    }

    private String getRedirectUrlToAffectedCategory(Category category) {
        return "redirect:/categories/page/1?sortField=id&sortDir=asc&keyword=";
    }

    @GetMapping("/{id}")
    public String editCategory(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.getCategoryById(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category");
            return "categories/category-form";
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteById(id);

            String categoryDir = "category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message", "The category ID " + id + " has been deleted successfully");
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/categories";
    }

    @GetMapping("/{id}/enabled/{enabled}")
    public String updateCategoryEnabledStatus(@PathVariable Long id,
                                              @PathVariable boolean enabled,
                                              RedirectAttributes redirectAttributes) {

        categoryService.updateEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";

        String message = "The category ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> categories = categoryService.listCategoriesUsedInForm();
        CategoryCsvExporter exporter = new CategoryCsvExporter();

        exporter.export(categories, response);
    }

}
