package com.shopme.product.controller;

import com.shopme.category.service.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.product.service.ProductService;
import com.shopme.product.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable String category_alias,
                                     Model model) {
        return viewCategoryByPage(category_alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable String category_alias,
                               @PathVariable int pageNum,
                               Model model){
        try {
            Category category = categoryService.getCategoryByAlias(category_alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(category);
            Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
            List<Product> productList = pageProducts.getContent();

            long startCount = (long) (pageNum - 1) * ProductServiceImpl.PRODUCTS_PER_PAGE + 1;
            long endCount = startCount + ProductServiceImpl.PRODUCTS_PER_PAGE - 1;
            if (endCount > pageProducts.getTotalElements()) {
                endCount = pageProducts.getTotalElements();
            }

            model.addAttribute("productList", productList);
            model.addAttribute("totalItems", pageProducts.getTotalElements());
            model.addAttribute("totalPages", pageProducts.getTotalPages());
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("category", category);

            return "products/products-by-category";
        }catch (CategoryNotFoundException e){
            return "error/404";
        }
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable String product_alias, Model model){
        try {
            Product product = productService.findByAlias(product_alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());

            model.addAttribute("product", product);
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("pageTitle", product.getShortName());

            return "products/product-detail";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(@Param("keyword") String keyword,
                         Model model){
        return searchByPage(keyword,1, model);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@Param("keyword") String keyword,
                         @PathVariable int pageNum,
                         Model model){
        Page<Product> pageProducts = productService.search(keyword, pageNum);
        List<Product> resultList = pageProducts.getContent();

        long startCount = (long) (pageNum - 1) * ProductServiceImpl.SEARCH_RESULT_PER_PAGE + 1;
        long endCount = startCount + ProductServiceImpl.SEARCH_RESULT_PER_PAGE - 1;
        if (endCount > pageProducts.getTotalElements()) {
            endCount = pageProducts.getTotalElements();
        }

        model.addAttribute("pageTitle", keyword + " - Search Result");
        model.addAttribute("keyword", keyword);
        model.addAttribute("resultList", resultList);

        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("currentPage", pageNum);


        return "products/search_result";
    }

}
