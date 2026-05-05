package com.sugarcorner.controller;

import com.sugarcorner.dto.RegisterRequest;
import com.sugarcorner.model.entity.User;
import com.sugarcorner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest("", "", "", "", "", ""));
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            User user = userService.register(registerRequest);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }
}
