package com.gyms.memberships_manager.controller;

import com.gyms.memberships_manager.dto.RevenueReportResponse;
import com.gyms.memberships_manager.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    @GetMapping("/revenue")
    public List<RevenueReportResponse> getRevenueReport() {
        return reportService.getRevenueReport();
    }
}