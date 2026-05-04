package com.gyms.memberships_manager.dto;

import jakarta.validation.constraints.NotBlank;

public record GymRequest(
    @NotBlank(message = "Gym name is required")
    String name,

    String address,
    String phoneNumber
) {}
