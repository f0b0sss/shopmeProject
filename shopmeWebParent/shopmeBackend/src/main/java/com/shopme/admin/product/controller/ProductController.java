package com.shopme.admin.product.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.service.BrandService;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.product.service.ProductService;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;

    @GetMapping
    public String listAllFirstPage() {
        return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
    }

    @GetMapping("/page/{pageNum}")
    public String listAllByPage(@PagingAndSortingParam(listName = "productList", moduleUrl = "/products") PagingAndSortingHelper helper,
                                @PathVariable int pageNum,
                                @Param("categoryId") Long categoryId, Model model) {
        productService.listAllByPage(pageNum, helper, categoryId);

        List<Category> categoryList = categoryService.listCategoriesUsedInForm();

        if (categoryId != null) {
            model.addAttribute("categoryId", categoryId);
        }

        model.addAttribute("categoryList", categoryList);

        return "products/products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        List<Brand> brandList = brandService.listAll();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("brandList", brandList);
        model.addAttribute("product", product);
        model.addAttribute("numberOfExistingExtraImages", 0);
        model.addAttribute("pageTitle", "Create New Product");

        return "products/product-form";
    }

    @PostMapping("/save")
    public String saveProduct(Product product,
                              @RequestParam(required = false) MultipartFile fileImage,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImages,
                              @RequestParam(name = "detailIds", required = false) String[] detailIds,
                              @RequestParam(required = false) String[] detailNames,
                              @RequestParam(required = false) String[] detailValues,
                              @RequestParam(name = "imageIds", required = false) String[] imageIds,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                              RedirectAttributes redirectAttributes) throws IOException {
        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
            if (loggedUser.hasRole("Salesperson")) {
                productService.saveProductPrice(product);

                redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");

                return "redirect:/products";
            }
        }

        ProductSaveHelper.setMainImageName(product, fileImage);
        ProductSaveHelper.setExistingExtraImageNames(imageIds, imageNames, product);
        ProductSaveHelper.setNewExtraImagesNames(product, extraImages);
        ProductSaveHelper.setProductDetails(detailIds, detailNames, detailValues, product);

        Product savedProduct = productService.save(product);

        ProductSaveHelper.saveUploadedImages(fileImage, extraImages, savedProduct);

        if (product.getId() != null) {
            ProductSaveHelper.deleteExtraImagesWhereRemovedOnForm(product);
        }

        productService.save(product);

        redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");

        return "redirect:/products";
    }


    @GetMapping("/{id}")
    public String editProduct(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
        try {
            Product product = productService.get(id);
            List<Brand> brandList = brandService.listAll();
            Integer numberOfExistingExtraImages = product.getImages().size();

            boolean isReadOnlyForSalesperson = false;

            if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
                if (loggedUser.hasRole("Salesperson")) {
                    isReadOnlyForSalesperson = true;
                }
            }

            model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
            model.addAttribute("product", product);
            model.addAttribute("brandList", brandList);
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");

            return "products/product-form";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/detail/{id}")
    public String viewProductDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);

            model.addAttribute("product", product);

            return "products/product-detail-modal";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            String productExtraImagesDir = "product-images/" + id + "/extras";
            String productImagesDir = "product-images/" + id;

            FileUploadUtil.removeDir(productExtraImagesDir);
            FileUploadUtil.removeDir(productImagesDir);


            redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully");
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/{id}/enabled/{enabled}")
    public String updateProductEnabledStatus(@PathVariable Long id,
                                             @PathVariable boolean enabled,
                                             RedirectAttributes redirectAttributes) {

        productService.updateEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";

        String message = "The product ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/products";
    }


}
