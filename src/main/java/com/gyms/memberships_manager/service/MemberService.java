package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.MemberRegistrationRequest;
import com.gyms.memberships_manager.dto.MemberResponse;

import java.util.List;

public interface MemberService {
    MemberResponse registerMember(MemberRegistrationRequest request);

    List<MemberResponse> getAllMembers();

    MemberResponse cancelMembership(Long memberId);
}
