package com.sugarcorner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FeedbackRequest(
        @NotBlank @Size(min = 5, max = 200) String subject,
        @NotBlank @Size(min = 10, max = 2000) String message
) {}
