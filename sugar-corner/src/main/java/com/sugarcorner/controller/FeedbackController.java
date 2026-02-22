package com.sugarcorner.controller;

import com.sugarcorner.dto.FeedbackRequest;
import com.sugarcorner.model.entity.User;
import com.sugarcorner.service.FeedbackService;
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
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    public FeedbackController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @GetMapping
    public String feedbackForm(Model model) {
        model.addAttribute("feedbackRequest", new FeedbackRequest("", ""));
        return "feedback/form";
    }

    @PostMapping
    public String submitFeedback(@Valid @ModelAttribute FeedbackRequest feedbackRequest,
                                 BindingResult result,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "feedback/form";
        }
        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("error", "Please login to submit feedback.");
            return "redirect:/login";
        }
        User customer = userService.findByEmail(userDetails.getUsername());
        feedbackService.submitFeedback(customer, feedbackRequest);
        redirectAttributes.addFlashAttribute("success", "Thank you for your feedback!");
        return "redirect:/feedback";
    }

    @GetMapping("/history")
    public String feedbackHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User customer = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("feedbacks", feedbackService.getByCustomer(customer.getId()));
        return "feedback/history";
    }
}
