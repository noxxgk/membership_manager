package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.MemberRegistrationRequest;
import com.gyms.memberships_manager.dto.MemberResponse;
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

    @InjectMocks
    private MemberServiceImpl memberService;

    private MembershipPlan dummyPlan;
    private Member dummyMember;
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
    }

    @Test
    void shouldRegisterMemberWhenCapacityIsNotReached() {
        MemberRegistrationRequest request = new MemberRegistrationRequest("John Doe", "john@test.com", 1L);

        when(planRepository.findByIdWithLock(1L)).thenReturn(Optional.of(dummyPlan));
        when(memberRepository.countByMembershipPlanIdAndStatus(1L, MemberStatus.ACTIVE)).thenReturn(9L);
        when(memberRepository.save(any(Member.class))).thenReturn(dummyMember);
        MemberResponse response = memberService.registerMember(request);
        assertNotNull(response);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void shouldThrowExceptionWhenRegisteringAndCapacityIsFull() {
        MemberRegistrationRequest request = new MemberRegistrationRequest("Jane Doe", "jane@test.com", 1L);

        when(planRepository.findByIdWithLock(1L)).thenReturn(Optional.of(dummyPlan));
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
        when(memberRepository.countByMembershipPlanIdAndStatus(1L, MemberStatus.ACTIVE)).thenReturn(9L);
        when(memberRepository.save(any(Member.class))).thenReturn(dummyMember);

        MemberResponse response = memberService.registerMember(request);

        assertNotNull(response);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

}