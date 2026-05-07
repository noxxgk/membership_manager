package com.gyms.memberships_manager.dto;

import java.math.BigDecimal;

public record RevenueReportResponse(
        String gymName,
        BigDecimal amount,
        String currency
) {
}