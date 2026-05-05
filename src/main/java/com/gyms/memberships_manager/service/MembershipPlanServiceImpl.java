package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.MembershipPlanRequest;
import com.gyms.memberships_manager.dto.MembershipPlanResponse;
import com.gyms.memberships_manager.exception.ResourceNotFoundException;
import com.gyms.memberships_manager.model.Gym;
import com.gyms.memberships_manager.model.MembershipPlan;
import com.gyms.memberships_manager.repository.GymRepository;
import com.gyms.memberships_manager.repository.MembershipPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository planRepository;
    private final GymRepository gymRepository;
    public MembershipPlanServiceImpl(MembershipPlanRepository planRepository, GymRepository gymRepository) {
        this.planRepository = planRepository;
        this.gymRepository = gymRepository;
    }

    @Override
    public MembershipPlanResponse createPlan(Long gymId, MembershipPlanRequest request) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new IllegalArgumentException("Gym with ID " + gymId + " not found."));
        MembershipPlan plan = new MembershipPlan();
        plan.setName(request.name());
        plan.setType(request.type());
        plan.setMonthlyPriceAmount(request.monthlyPriceAmount());
        plan.setCurrency(request.currency());
        plan.setDurationMonths(request.durationMonths());
        plan.setMaxMembers(request.maxMembers());
        plan.setGym(gym);
        MembershipPlan savedPlan = planRepository.save(plan);
        return mapToResponse(savedPlan);
    }

    @Override
    public List<MembershipPlanResponse> getPlansByGymId(Long gymId) {
        if (!gymRepository.existsById(gymId)) {
            throw new ResourceNotFoundException("Gym with ID " + gymId + " not found.");
        }

        return planRepository.findByGymId(gymId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    private MembershipPlanResponse mapToResponse(MembershipPlan plan) {
        return new MembershipPlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getType(),
                plan.getMonthlyPriceAmount(),
                plan.getCurrency(),
                plan.getDurationMonths(),
                plan.getMaxMembers()
        );
    }
}