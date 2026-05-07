package com.gyms.memberships_manager.dto;

import com.gyms.memberships_manager.model.MemberStatus;

import java.time.LocalDate;

public record MemberResponse(
        Long id,
        String fullName,
        String email,
        LocalDate startDate,
        MemberStatus status,
        String planName,
        String gymName
) {
}
