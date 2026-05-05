package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.RevenueReportResponse;
import com.gyms.memberships_manager.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void shouldReturnRevenueReport() {
        RevenueReportResponse report1 = new RevenueReportResponse("FitLife Center", new BigDecimal("2048.00"), "EUR");
        RevenueReportResponse report2 = new RevenueReportResponse("Iron Gym", new BigDecimal("1024.00"), "PLN");

        when(memberRepository.getRevenueReport()).thenReturn(List.of(report1, report2));
        List<RevenueReportResponse> result = reportService.getRevenueReport();
        assertEquals(2, result.size());
        assertEquals("FitLife Center", result.get(0).gymName());
        assertEquals("EUR", result.get(0).currency());
        assertEquals(new BigDecimal("1024.00"), result.get(1).amount());
    }
}