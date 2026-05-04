package com.gyms.memberships_manager.dto;

import com.gyms.memberships_manager.model.PlanType;
import java.math.BigDecimal;

public record MembershipPlanResponse(
        Long id,
        String name,
        PlanType type,
        BigDecimal monthlyPriceAmount,
        String currency,
        Integer durationMonths,
        Integer maxMembers
) {}
