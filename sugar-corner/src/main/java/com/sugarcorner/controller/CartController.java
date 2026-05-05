package com.sugarcorner.controller;

import com.sugarcorner.dto.CartItemDto;
import com.sugarcorner.dto.OrderRequest;
import com.sugarcorner.model.entity.Product;
import com.sugarcorner.service.CartService;
import com.sugarcorner.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping
    public String cart(Model model) {
        Map<Long, Integer> items = cartService.getItems();
        List<CartItemDto> cartItems = new ArrayList<>();
        for (Map.Entry<Long, Integer> e : items.entrySet()) {
            Product p = productService.getById(e.getKey());
            BigDecimal subtotal = p.getPrice().multiply(BigDecimal.valueOf(e.getValue()));
            cartItems.add(new CartItemDto(p, e.getValue(), subtotal));
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartService.getTotal());
        model.addAttribute("orderRequest", new OrderRequest(cartService.getOrderItems(), "", ""));
        return "cart/view";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity,
                            @RequestHeader(value = "Referer", required = false) String referer) {
        cartService.addItem(productId, quantity);
        return "redirect:" + (referer != null ? referer : "/products");
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeItem(productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart() {
        cartService.clear();
        return "redirect:/cart";
    }
}
