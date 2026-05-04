package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.MembershipPlanRequest;
import com.gyms.memberships_manager.dto.MembershipPlanResponse;

import java.util.List;

public interface MembershipPlanService {
    MembershipPlanResponse createPlan(Long gymId, MembershipPlanRequest request);
    List<MembershipPlanResponse> getPlansByGymId(Long gymId);
}
