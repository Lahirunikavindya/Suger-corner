package com.sugarcorner.controller;

import com.sugarcorner.model.entity.Product;
import com.sugarcorner.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(@RequestParam(required = false) String search, Model model) {
        List<Product> products = productService.searchProducts(search);
        model.addAttribute("products", products);
        model.addAttribute("search", search);
        return "products/list";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        model.addAttribute("product", product);
        return "products/detail";
    }
}
