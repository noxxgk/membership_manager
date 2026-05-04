package com.gyms.memberships_manager.repository;

import com.gyms.memberships_manager.model.Member;
import com.gyms.memberships_manager.model.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    long countByMembershipPlanIdAndStatus(Long membershipPlanId, MemberStatus status);

}
