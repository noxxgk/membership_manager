package com.gyms.memberships_manager.controller;

import com.gyms.memberships_manager.dto.MembershipPlanRequest;
import com.gyms.memberships_manager.dto.MembershipPlanResponse;
import com.gyms.memberships_manager.service.MembershipPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gyms/{gymId}/plans")
public class MembershipPlanController {

    private final MembershipPlanService planService;

    public MembershipPlanController(MembershipPlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipPlanResponse createPlan(
            @PathVariable Long gymId,
            @Valid @RequestBody MembershipPlanRequest request) {
        return planService.createPlan(gymId, request);
    }

    @GetMapping
    public List<MembershipPlanResponse> getPlansByGymId(@PathVariable Long gymId) {
        return planService.getPlansByGymId(gymId);
    }
}
