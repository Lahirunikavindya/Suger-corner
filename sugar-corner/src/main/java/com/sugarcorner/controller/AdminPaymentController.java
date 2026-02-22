package com.sugarcorner.controller;

import com.sugarcorner.model.entity.Payment;
import com.sugarcorner.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/payments")
public class AdminPaymentController {

    private final PaymentService paymentService;

    public AdminPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public String listPayments(@RequestParam(required = false) Payment.PaymentStatus status, Model model) {
        List<Payment> payments = status != null
                ? paymentService.getByStatus(status)
                : paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        model.addAttribute("statusFilter", status);
        return "admin/payments/list";
    }

    @PostMapping("/{id}/mark-paid")
    public String markAsPaid(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        paymentService.markAsPaid(id);
        redirectAttributes.addFlashAttribute("success", "Payment marked as paid.");
        return "redirect:/admin/payments";
    }
}
