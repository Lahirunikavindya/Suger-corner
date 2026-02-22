package com.sugarcorner.controller;

import com.sugarcorner.model.entity.Feedback;
import com.sugarcorner.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/feedback")
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    public AdminFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String listFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "admin/feedback/list";
    }

    @GetMapping("/{id}")
    public String feedbackDetail(@PathVariable Long id, Model model) {
        model.addAttribute("feedback", feedbackService.getById(id));
        return "admin/feedback/detail";
    }

    @PostMapping("/{id}/respond")
    public String respond(@PathVariable Long id,
                          @RequestParam String adminResponse,
                          RedirectAttributes redirectAttributes) {
        feedbackService.respond(id, adminResponse);
        redirectAttributes.addFlashAttribute("success", "Response submitted.");
        return "redirect:/admin/feedback/" + id;
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam Feedback.MessageStatus status,
                               RedirectAttributes redirectAttributes) {
        feedbackService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Status updated.");
        return "redirect:/admin/feedback/" + id;
    }
}
