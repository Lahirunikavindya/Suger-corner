package com.sugarcorner.controller;

import com.sugarcorner.dto.OrderRequest;
import com.sugarcorner.model.entity.User;
import com.sugarcorner.service.CartService;
import com.sugarcorner.service.OrderService;
import com.sugarcorner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    public OrderController(OrderService orderService, UserService userService, CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping
    public String orderHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User customer = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("orders", orderService.getOrdersByCustomer(customer));
        return "orders/history";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        User customer = userService.findByEmail(userDetails.getUsername());
        var order = orderService.getById(id);
        if (!order.getCustomer().getId().equals(customer.getId())) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "orders/detail";
    }

    @PostMapping
    public String placeOrder(@Valid @ModelAttribute OrderRequest orderRequest,
                             BindingResult result,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors() || orderRequest.items().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Invalid order. Please add items to your cart.");
            return "redirect:/cart";
        }
        User customer = userService.findByEmail(userDetails.getUsername());
        try {
            var order = orderService.placeOrder(customer, orderRequest);
            cartService.clear();
            redirectAttributes.addFlashAttribute("success", "Order placed successfully! Order #" + order.getOrderNumber());
            return "redirect:/orders/" + order.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }
}
