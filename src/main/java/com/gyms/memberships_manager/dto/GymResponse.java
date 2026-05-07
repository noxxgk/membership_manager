package com.gyms.memberships_manager.dto;

public record GymResponse(
        Long id,
        String name,
        String address,
        String phoneNumber
) {
}

