package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.RevenueReportResponse;
import com.gyms.memberships_manager.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final MemberRepository memberRepository;

    public List<RevenueReportResponse> getRevenueReport() {
        log.info("Fetching revenue report");
        return memberRepository.getRevenueReport();
    }
}