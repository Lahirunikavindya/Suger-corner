package com.sugarcorner.controller;

import com.sugarcorner.dto.ProfileUpdateRequest;
import com.sugarcorner.model.entity.User;
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
@RequestMapping("/profile")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("profileUpdateRequest", new ProfileUpdateRequest(
                user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getAddress()));
        return "user/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute ProfileUpdateRequest profileUpdateRequest,
                                BindingResult result,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/profile";
        }
        User user = userService.findByEmail(userDetails.getUsername());
        userService.updateProfile(user.getId(), profileUpdateRequest);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/profile";
    }
}
