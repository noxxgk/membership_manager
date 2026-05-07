package com.gyms.memberships_manager.dto;

import jakarta.validation.constraints.NotBlank;

public record GymRequest(
        @NotBlank(message = "Gym name is required")
        String name,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Phone number is required")
        String phoneNumber
) {
}
