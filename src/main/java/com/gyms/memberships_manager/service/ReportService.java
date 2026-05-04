package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.RevenueReportResponse;
import com.gyms.memberships_manager.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final MemberRepository memberRepository;

    public ReportService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    public List<RevenueReportResponse> getRevenueReport() {
        return memberRepository.getRevenueReport();
    }
}