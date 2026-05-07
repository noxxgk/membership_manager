package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.MemberRegistrationRequest;
import com.gyms.memberships_manager.dto.MemberResponse;
import com.gyms.memberships_manager.mapper.MemberMapper;
import com.gyms.memberships_manager.model.Gym;
import com.gyms.memberships_manager.model.Member;
import com.gyms.memberships_manager.model.MemberStatus;
import com.gyms.memberships_manager.model.MembershipPlan;
import com.gyms.memberships_manager.repository.MemberRepository;
import com.gyms.memberships_manager.repository.MembershipPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MembershipPlanRepository planRepository;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    private MembershipPlan dummyPlan;
    private Member dummyMember;
    private MemberResponse dummyResponse;

    @BeforeEach
    void setUp() {
        Gym gym = new Gym();
        gym.setName("Test Gym");

        dummyPlan = new MembershipPlan();
        dummyPlan.setId(1L);
        dummyPlan.setName("Test Plan");
        dummyPlan.setMaxMembers(10);
        dummyPlan.setGym(gym);

        dummyMember = new Member();
        dummyMember.setId(100L);
        dummyMember.setMembershipPlan(dummyPlan);
        dummyMember.setStatus(MemberStatus.ACTIVE);

        dummyResponse = new MemberResponse(100L, "Test Member", "test@test.com", null, MemberStatus.ACTIVE, "Test Plan", "Test Gym");
    }

    @Test
    void shouldRegisterMemberWhenCapacityIsNotReached() {
        MemberRegistrationRequest request = new MemberRegistrationRequest("John Doe", "john@test.com", 1L);

        when(planRepository.findByIdWithLock(1L)).thenReturn(Optional.of(dummyPlan));
        when(memberRepository.existsByFullNameAndEmail("John Doe", "john@test.com")).thenReturn(false);
        when(memberRepository.countByMembershipPlanIdAndStatus(1L, MemberStatus.ACTIVE)).thenReturn(9L);
        when(memberMapper.toEntity(any(MemberRegistrationRequest.class))).thenReturn(new Member());
        when(memberRepository.save(any(Member.class))).thenReturn(dummyMember);
        when(memberMapper.toResponse(any(Member.class))).thenReturn(dummyResponse);
        MemberResponse response = memberService.registerMember(request);
        assertNotNull(response);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void shouldThrowExceptionWhenRegisteringAndCapacityIsFull() {
        MemberRegistrationRequest request = new MemberRegistrationRequest("Jane Doe", "jane@test.com", 1L);

        when(planRepository.findByIdWithLock(1L)).thenReturn(Optional.of(dummyPlan));
        when(memberRepository.existsByFullNameAndEmail("Jane Doe", "jane@test.com")).thenReturn(false);
        when(memberRepository.countByMembershipPlanIdAndStatus(1L, MemberStatus.ACTIVE)).thenReturn(10L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.registerMember(request);
        });

        assertTrue(exception.getMessage().contains("maximum capacity"));
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void shouldThrowExceptionWhenCancellingAlreadyCancelledMembership() {
        dummyMember.setStatus(MemberStatus.CANCELLED);
        when(memberRepository.findById(100L)).thenReturn(Optional.of(dummyMember));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.cancelMembership(100L);
        });

        assertEquals("Membership is already cancelled.", exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void shouldRegisterMemberWhenCapacityWasFullButSomeoneCancelled() {
        MemberRegistrationRequest request = new MemberRegistrationRequest("New Guy", "new@test.com", 1L);

        when(planRepository.findByIdWithLock(1L)).thenReturn(Optional.of(dummyPlan));
        when(memberRepository.existsByFullNameAndEmail("New Guy", "new@test.com")).thenReturn(false);
        when(memberRepository.countByMembershipPlanIdAndStatus(1L, MemberStatus.ACTIVE)).thenReturn(9L);
        when(memberMapper.toEntity(any(MemberRegistrationRequest.class))).thenReturn(new Member());
        when(memberRepository.save(any(Member.class))).thenReturn(dummyMember);
        when(memberMapper.toResponse(any(Member.class))).thenReturn(dummyResponse);

        MemberResponse response = memberService.registerMember(request);

        assertNotNull(response);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void shouldThrowExceptionWhenRegisteringDuplicateMember() {
        MemberRegistrationRequest request = new MemberRegistrationRequest("Jane Doe", "jane@test.com", 1L);

        when(planRepository.findByIdWithLock(1L)).thenReturn(Optional.of(dummyPlan));
        when(memberRepository.existsByFullNameAndEmail("Jane Doe", "jane@test.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.registerMember(request);
        });

        assertTrue(exception.getMessage().contains("already exists"));
        verify(memberRepository, never()).save(any(Member.class));
    }
}