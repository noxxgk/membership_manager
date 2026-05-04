package com.gyms.memberships_manager.dto;

import com.gyms.memberships_manager.model.PlanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MembershipPlanRequest(
        @NotBlank(message = "Plan name is required")
        String name,

        @NotNull(message = "Plan type is required")
        PlanType type,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal monthlyPriceAmount,

        @NotBlank(message = "Currency is required")
        String currency,

        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be greater than zero")
        Integer durationMonths,

        @NotNull(message = "Max members limit is required")
        @Positive(message = "Max members must be greater than zero")
        Integer maxMembers
) {}
