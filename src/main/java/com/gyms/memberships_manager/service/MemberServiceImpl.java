package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.MemberRegistrationRequest;
import com.gyms.memberships_manager.dto.MemberResponse;
import com.gyms.memberships_manager.exception.ResourceNotFoundException;
import com.gyms.memberships_manager.mapper.MemberMapper;
import com.gyms.memberships_manager.model.Member;
import com.gyms.memberships_manager.model.MemberStatus;
import com.gyms.memberships_manager.model.MembershipPlan;
import com.gyms.memberships_manager.repository.MemberRepository;
import com.gyms.memberships_manager.repository.MembershipPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MembershipPlanRepository planRepository;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public MemberResponse registerMember(MemberRegistrationRequest request) {
        log.info("Attempting to register a new member with email: {}", request.email());
        MembershipPlan plan = planRepository.findByIdWithLock(request.membershipPlanId())
                .orElseThrow(() -> {
                    log.warn("Membership plan with ID {} not found.", request.membershipPlanId());
                    return new ResourceNotFoundException("Membership plan with ID " + request.membershipPlanId() + " not found.");
                });
        if (memberRepository.existsByFullNameAndEmail(request.fullName(), request.email())) {
            log.warn("Member with full name '{}' and email '{}' already exists.", request.fullName(), request.email());
            throw new IllegalArgumentException("Member with full name '" + request.fullName() + "' and email '" + request.email() + "' already exists.");
        }
        long currentActiveMembers = memberRepository.countByMembershipPlanIdAndStatus(plan.getId(), MemberStatus.ACTIVE);
        if (currentActiveMembers >= plan.getMaxMembers()) {
            log.warn("Registration failed. Plan {} has reached maximum capacity.", plan.getId());
            throw new IllegalArgumentException("Cannot register. Membership plan has reached its maximum capacity of " + plan.getMaxMembers() + " active members.");
        }
        Member member = memberMapper.toEntity(request);
        member.setMembershipPlan(plan);
        member.setStartDate(LocalDate.now());
        member.setStatus(MemberStatus.ACTIVE);
        Member savedMember = memberRepository.save(member);
        log.info("Successfully registered member {} to plan {}", member.getEmail(), plan.getId());
        return memberMapper.toResponse(savedMember);
    }

    @Override
    public List<MemberResponse> getAllMembers() {
        log.info("Fetching all members");
        return memberRepository.findAllWithPlanAndGym()
                .stream()
                .map(memberMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public MemberResponse cancelMembership(Long memberId) {
        log.info("Attempting to cancel membership for member ID: {}", memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("Member with ID {} not found.", memberId);
                    return new ResourceNotFoundException("Member with ID " + memberId + " not found.");
                });

        if (member.getStatus() == MemberStatus.CANCELLED) {
            log.warn("Membership for member ID {} is already cancelled.", memberId);
            throw new IllegalArgumentException("Membership is already cancelled.");
        }
        member.setStatus(MemberStatus.CANCELLED);
        Member savedMember = memberRepository.save(member);
        log.info("Successfully cancelled membership for member ID: {}", memberId);
        return memberMapper.toResponse(savedMember);
    }
}