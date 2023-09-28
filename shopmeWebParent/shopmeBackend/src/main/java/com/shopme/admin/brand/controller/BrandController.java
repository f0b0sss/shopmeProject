package com.shopme.admin.brand.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.service.BrandService;
import com.shopme.admin.brand.service.BrandServiceImpl;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("brands")
public class BrandController {

    @Autowired
    private BrandService service;

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public String listAll(Model model){
        return listAllByPage(1, "name", "asc", null, model);
    }

    @GetMapping("/page/{pageNum}")
    public String listAllByPage(@PathVariable int pageNum,
                                @Param("sortField") String sortField,
                                @Param("sortDir") String sortDir,
                                @Param("keyword") String keyword,
                                Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        Page<Brand> page = service.listAllByPage(pageNum, sortField, sortDir, keyword);
        List<Brand> brandList = page.getContent();

        long startCount = (long) (pageNum - 1) * BrandServiceImpl.BRANDS_PER_PAGE + 1;
        long endCount = startCount + BrandServiceImpl.BRANDS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("brandList", brandList);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleUrl", "/brands");

        return "brands/brands";
    }

    @GetMapping("/new")
    public String newBrand(Model model){
        List<Category> categoryList = categoryService.listCategoriesUsedInForm();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Brand");

        return "brands/brand-form";
    }

    @PostMapping("/save")
    public String saveBrand(Brand brand,
                            RedirectAttributes redirectAttributes,
                            @RequestParam MultipartFile fileImage) throws IOException {
        if (!fileImage.isEmpty()) {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(fileImage.getOriginalFilename()));

            brand.setLogo(filename);

            Brand savedBrand = service.save(brand);

            String uploadDir = "brand-logos/" + savedBrand.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, fileImage);
        } else {
            service.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully.");

        return "redirect:/brands";
    }

    @GetMapping("/{id}")
    public String editBrand(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        try {
            Brand brand = service.get(id);
            List<Category> categoryList = categoryService.listCategoriesUsedInForm();

            model.addAttribute("brand", brand);
            model.addAttribute("categoryList", categoryList);
            model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");

            return "brands/brand-form";
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.deleteById(id);

            String categoryDir = "brand-logos/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message", "The brand ID " + id + " has been deleted successfully");
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/brands";
    }


}
