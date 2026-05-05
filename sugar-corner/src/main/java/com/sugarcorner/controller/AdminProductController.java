package com.sugarcorner.controller;

import com.sugarcorner.model.entity.Product;
import com.sugarcorner.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/products/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Product product,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/products/form";
        }
        productService.create(product);
        redirectAttributes.addFlashAttribute("success", "Product created successfully.");
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getById(id));
        return "admin/products/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                        @Valid @ModelAttribute Product product,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/products/form";
        }
        productService.update(id, product);
        redirectAttributes.addFlashAttribute("success", "Product updated successfully.");
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Product deleted successfully.");
        return "redirect:/admin/products";
    }
}
