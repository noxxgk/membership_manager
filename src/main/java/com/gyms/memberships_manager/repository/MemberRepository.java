package com.gyms.memberships_manager.repository;

import com.gyms.memberships_manager.dto.RevenueReportResponse;
import com.gyms.memberships_manager.model.Member;
import com.gyms.memberships_manager.model.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    long countByMembershipPlanIdAndStatus(Long membershipPlanId, MemberStatus status);

    boolean existsByFullNameAndEmail(String fullName, String email);

    @Query("SELECT m FROM Member m JOIN FETCH m.membershipPlan p JOIN FETCH p.gym")
    List<Member> findAllWithPlanAndGym();

    @Query("SELECT new com.gyms.memberships_manager.dto.RevenueReportResponse(g.name, SUM(p.monthlyPriceAmount), p.currency) " +
            "FROM Member m " +
            "JOIN m.membershipPlan p " +
            "JOIN p.gym g " +
            "WHERE m.status = com.gyms.memberships_manager.model.MemberStatus.ACTIVE " +
            "GROUP BY g.name, p.currency")
    List<RevenueReportResponse> getRevenueReport();
}
