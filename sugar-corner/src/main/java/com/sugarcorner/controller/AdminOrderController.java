package com.sugarcorner.controller;

import com.sugarcorner.model.entity.Order;
import com.sugarcorner.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getById(id));
        return "admin/orders/detail";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                              @RequestParam Order.OrderStatus status,
                              RedirectAttributes redirectAttributes) {
        orderService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Order status updated.");
        return "redirect:/admin/orders/" + id;
    }
}
