package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.MemberRegistrationRequest;
import com.gyms.memberships_manager.dto.MemberResponse;
import com.gyms.memberships_manager.exception.ResourceNotFoundException;
import com.gyms.memberships_manager.model.Member;
import com.gyms.memberships_manager.model.MemberStatus;
import com.gyms.memberships_manager.model.MembershipPlan;
import com.gyms.memberships_manager.repository.MemberRepository;
import com.gyms.memberships_manager.repository.MembershipPlanRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MembershipPlanRepository planRepository;

    public MemberServiceImpl(MemberRepository memberRepository, MembershipPlanRepository planRepository) {
        this.memberRepository = memberRepository;
        this.planRepository = planRepository;
    }

    @Override
    @Transactional
    public MemberResponse registerMember(MemberRegistrationRequest request) {
        MembershipPlan plan = planRepository.findByIdWithLock(request.membershipPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Membership plan with ID " + request.membershipPlanId() + " not found."));
        long currentActiveMembers = memberRepository.countByMembershipPlanIdAndStatus(plan.getId(), MemberStatus.ACTIVE);
        if (currentActiveMembers >= plan.getMaxMembers()) {
            throw new IllegalArgumentException("Cannot register. Membership plan has reached its maximum capacity of " + plan.getMaxMembers() + " active members.");
        }
        Member member = new Member();
        member.setFullName(request.fullName());
        member.setEmail(request.email());
        member.setMembershipPlan(plan);
        member.setStartDate(LocalDate.now());
        member.setStatus(MemberStatus.ACTIVE);
        Member savedMember = memberRepository.save(member);
        return mapToResponse(savedMember);
    }
    @Override
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAllWithPlanAndGym()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    @Transactional
    public MemberResponse cancelMembership(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member with ID " + memberId + " not found."));

        if (member.getStatus() == MemberStatus.CANCELLED) {
            throw new IllegalArgumentException("Membership is already cancelled.");
        }
        member.setStatus(MemberStatus.CANCELLED);
        Member savedMember = memberRepository.save(member);
        return mapToResponse(savedMember);
    }
    private MemberResponse mapToResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getFullName(),
                member.getEmail(),
                member.getStartDate(),
                member.getStatus(),
                member.getMembershipPlan().getName(),
                member.getMembershipPlan().getGym().getName()
        );
    }
}